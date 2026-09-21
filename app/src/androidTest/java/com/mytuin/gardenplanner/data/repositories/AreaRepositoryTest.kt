package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AreaRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: AreaRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db =
                Room
                    .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                    .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                    .build()
            repository = AreaRepositoryImpl(areaDao = db.areaDao())

            db.gardenDao().insert(sampleGarden())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getArea_returns_null_when_no_area_exists() =
        runBlocking {
            assertNull(repository.getArea("area_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val area = sampleArea()
            repository.insert(area)

            assertEquals(area, repository.getArea(area.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleArea().copy(
                    id = "area_00000000-0000-0000-0000-00000000bb",
                    gardenId = "garden_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("garden_id", expected.field)
            }
        }

    @Test
    fun observeAreasInGarden_scopes_to_garden_and_orders_by_name() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(
                sampleGarden().copy(id = otherGarden, name = "Other Garden"),
            )

            repository.insert(
                sampleArea().copy(
                    id = "area_00000000-0000-0000-0000-00000000a1",
                    name = "Zeta",
                ),
            )
            repository.insert(
                sampleArea().copy(
                    id = "area_00000000-0000-0000-0000-00000000a2",
                    name = "Alpha",
                ),
            )
            repository.insert(
                sampleArea().copy(
                    id = "area_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                    name = "Other garden area",
                ),
            )

            val areas = repository.observeAreasInGarden(gardenId).first()

            assertEquals(2, areas.size)
            assertEquals("Alpha", areas[0].name)
            assertEquals("Zeta", areas[1].name)
        }

    @Test
    fun polygon_geometry_round_trips_through_geojson() =
        runBlocking {
            val geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(5.0, 0.0),
                        Coordinate(5.0, 4.0),
                        Coordinate(0.0, 4.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            val area = sampleArea().copy(geometry = geometry)
            repository.insert(area)

            assertEquals(geometry, repository.getArea(area.id)?.geometry)
        }

    @Test
    fun null_geometry_round_trips_as_null() =
        runBlocking {
            val area = sampleArea().copy(geometry = null)
            repository.insert(area)

            assertNull(repository.getArea(area.id)?.geometry)
        }

    @Test
    fun null_status_round_trips_as_null() =
        runBlocking {
            val area = sampleArea().copy(status = null)
            repository.insert(area)

            assertNull(repository.getArea(area.id)?.status)
        }

    @Test
    fun null_status_area_is_returned_by_active_observation() =
        runBlocking {
            // A6: null status means "no lifecycle state recorded",
            // treated as not-archived. A null-status area must appear
            // in the default (active) observation.
            repository.insert(sampleArea().copy(status = null))

            val areas = repository.observeAreasInGarden(gardenId).first()
            assertEquals(1, areas.size)
            assertNull(areas.first().status)
        }

    @Test
    fun area_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleArea().copy(areaType = AreaType.SECTION))

            db.openHelper.readableDatabase
                .query("SELECT area_type FROM area")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("section", cursor.getString(0))
                }
        }

    @Test
    fun archive_sets_status_and_excludes_from_default_observation() =
        runBlocking {
            val area = sampleArea()
            repository.insert(area)

            repository.archive(area.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getArea(area.id)?.status,
            )
            assertEquals(
                0,
                repository.observeAreasInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeAreasInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun restore_returns_the_area_to_active() =
        runBlocking {
            val area = sampleArea()
            repository.insert(area)
            repository.archive(area.id, 1_700_000_500_000L)

            repository.restore(area.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getArea(area.id)?.status,
            )
            assertEquals(
                1,
                repository.observeAreasInGarden(gardenId).first().size,
            )
        }

    @Test
    fun archive_throws_NotFoundError_when_area_missing() =
        runBlocking {
            try {
                repository.archive("area_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Area", expected.entityType)
            }
        }

    @Test
    fun restoring_an_area_does_not_affect_its_garden() =
        runBlocking {
            val area = sampleArea()
            repository.insert(area)
            repository.archive(area.id, 1_700_000_500_000L)
            repository.restore(area.id, 1_700_001_000_000L)

            val garden = db.gardenDao().getById(gardenId)
            assertEquals(RecordStatus.DRAFT, garden?.status)
        }

    private fun sampleGarden(): GardenEntity =
        GardenEntity(
            id = gardenId,
            name = "Test Garden",
            description = null,
            country_code = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
            hemisphere = Hemisphere.UNKNOWN,
            status = RecordStatus.DRAFT,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleArea(): Area =
        Area(
            id = "area_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            name = "Vegetable Garden",
            areaType = AreaType.ZONE,
            description = "North side",
            geometry = null,
            status = RecordStatus.ACTIVE,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
