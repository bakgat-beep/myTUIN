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
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
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
class GrowingSpaceRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: GrowingSpaceRepository

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
            repository =
                GrowingSpaceRepositoryImpl(
                    db = db,
                    growingSpaceDao = db.growingSpaceDao(),
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                    idGenerator =
                        com.mytuin.gardenplanner.platform.identifiers
                            .UuidIdGenerator(),
                )

            db.gardenDao().insert(sampleGarden())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getGrowingSpace_returns_null_when_no_space_exists() =
        runBlocking {
            assertNull(repository.getGrowingSpace("growingspace_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val space = sampleSpace()
            repository.insert(space)

            assertEquals(space, repository.getGrowingSpace(space.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleSpace().copy(
                    id = "growingspace_00000000-0000-0000-0000-00000000bb",
                    gardenId = "garden_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                // V1_DATABASE_SCHEMA §67: FK enforced. A98: surfaced as a
                // structured DomainError, not raw SQLiteConstraintException.
                assertEquals("garden_id", expected.field)
            }
        }

    @Test
    fun observeGrowingSpacesInGarden_scopes_to_garden_and_orders_by_name() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(
                sampleGarden().copy(
                    id = otherGarden,
                    name = "Other Garden",
                ),
            )

            repository.insert(
                sampleSpace().copy(
                    id = "growingspace_00000000-0000-0000-0000-00000000a1",
                    name = "Zeta",
                ),
            )
            repository.insert(
                sampleSpace().copy(
                    id = "growingspace_00000000-0000-0000-0000-00000000a2",
                    name = "Alpha",
                ),
            )
            repository.insert(
                sampleSpace().copy(
                    id = "growingspace_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                    name = "Other garden bed",
                ),
            )

            val spaces = repository.observeGrowingSpacesInGarden(gardenId).first()

            assertEquals(2, spaces.size)
            assertEquals("Alpha", spaces[0].name)
            assertEquals("Zeta", spaces[1].name)
        }

    @Test
    fun point_geometry_round_trips_through_geojson() =
        runBlocking {
            val geometry = Geometry.Point(Coordinate(1.5, 2.5))
            val space = sampleSpace().copy(geometry = geometry)
            repository.insert(space)

            assertEquals(geometry, repository.getGrowingSpace(space.id)?.geometry)
        }

    @Test
    fun line_string_geometry_round_trips_through_geojson() =
        runBlocking {
            val geometry =
                Geometry.LineString(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(1.0, 1.0),
                        Coordinate(2.0, 0.5),
                    ),
                )
            val space = sampleSpace().copy(geometry = geometry)
            repository.insert(space)

            assertEquals(geometry, repository.getGrowingSpace(space.id)?.geometry)
        }

    @Test
    fun polygon_geometry_round_trips_through_geojson() =
        runBlocking {
            val geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            val space = sampleSpace().copy(geometry = geometry)
            repository.insert(space)

            assertEquals(geometry, repository.getGrowingSpace(space.id)?.geometry)
        }

    @Test
    fun null_geometry_round_trips_as_null() =
        runBlocking {
            val space = sampleSpace().copy(geometry = null)
            repository.insert(space)

            assertNull(repository.getGrowingSpace(space.id)?.geometry)
        }

    @Test
    fun geometry_type_is_stored_as_canonical_id_not_geojson_name() =
        runBlocking {
            repository.insert(
                sampleSpace().copy(
                    geometry =
                        Geometry.Polygon(
                            listOf(
                                Coordinate(0.0, 0.0),
                                Coordinate(1.0, 0.0),
                                Coordinate(1.0, 1.0),
                                Coordinate(0.0, 0.0),
                            ),
                        ),
                ),
            )

            db.openHelper.readableDatabase
                .query("SELECT geometry_type, geometry_data FROM growing_space")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("polygon", cursor.getString(0))
                    val json = cursor.getString(1)
                    assertTrue(
                        "GeoJSON type name expected in data",
                        json.contains("\"type\":\"Polygon\""),
                    )
                }
        }

    @Test
    fun space_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleSpace())

            db.openHelper.readableDatabase
                .query("SELECT space_type FROM growing_space")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("raised_bed", cursor.getString(0))
                }
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

    private fun sampleSpace(): GrowingSpace =
        GrowingSpace(
            id = "growingspace_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            name = "Bed 2",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            geometry = null,
            lengthMetres = 3.0,
            widthMetres = 1.2,
            heightMetres = 0.4,
            diameterMetres = null,
            areaSquareMetres = 3.6,
            volumeCubicMetres = null,
            description = "Main raised bed",
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )

    @Test
    fun archive_sets_status_and_excludes_from_default_observation() =
        runBlocking {
            val space = sampleSpace()
            repository.insert(space)

            repository.archive(space.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getGrowingSpace(space.id)?.status,
            )
            assertEquals(
                0,
                repository.observeGrowingSpacesInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeGrowingSpacesInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun restore_returns_the_space_to_active() =
        runBlocking {
            val space = sampleSpace()
            repository.insert(space)
            repository.archive(space.id, 1_700_000_500_000L)

            repository.restore(space.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getGrowingSpace(space.id)?.status,
            )
            assertEquals(
                1,
                repository.observeGrowingSpacesInGarden(gardenId).first().size,
            )
        }

    @Test
    fun archive_throws_NotFoundError_when_space_missing() =
        runBlocking {
            try {
                repository.archive("growingspace_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("GrowingSpace", expected.entityType)
            }
        }

    @Test
    fun restoring_a_space_does_not_affect_its_garden() =
        runBlocking {
            val space = sampleSpace()
            repository.insert(space)
            repository.archive(space.id, 1_700_000_500_000L)
            repository.restore(space.id, 1_700_001_000_000L)

            // The garden row is untouched. Its status remains whatever it
            // was created with (DRAFT per sampleGarden in GardenRepositoryTest;
            // this test uses the garden from setUp).
            val garden = db.gardenDao().getById(gardenId)
            assertEquals(RecordStatus.DRAFT, garden?.status)
        }
}
