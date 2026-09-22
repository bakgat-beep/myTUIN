package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
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
class SpatialObjectRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: SpatialObjectRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val areaId = "area_00000000-0000-0000-0000-000000000001"

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
                SpatialObjectRepositoryImpl(
                    spatialObjectDao = db.spatialObjectDao(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.areaDao().insert(sampleArea())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getSpatialObject_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getSpatialObject("spatialobject_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val spatialObject = sampleSpatialObject()
            repository.insert(spatialObject)

            assertEquals(spatialObject, repository.getSpatialObject(spatialObject.id))
        }

    @Test
    fun insert_throws_ValidationError_when_garden_missing() =
        runBlocking {
            val orphan =
                sampleSpatialObject().copy(
                    id = "spatialobject_00000000-0000-0000-0000-00000000bb",
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
    fun insert_throws_ValidationError_when_area_missing() =
        runBlocking {
            val orphan =
                sampleSpatialObject().copy(
                    id = "spatialobject_00000000-0000-0000-0000-00000000cc",
                    areaId = "area_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("area_id", expected.field)
            }
        }

    @Test
    fun observeSpatialObjectsInGarden_scopes_to_garden_and_orders_by_name() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(
                sampleSpatialObject().copy(
                    id = "spatialobject_00000000-0000-0000-0000-00000000a1",
                    name = "Zeta path",
                ),
            )
            repository.insert(
                sampleSpatialObject().copy(
                    id = "spatialobject_00000000-0000-0000-0000-00000000a2",
                    name = "Alpha path",
                ),
            )
            repository.insert(
                sampleSpatialObject().copy(
                    id = "spatialobject_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                    name = "Other garden path",
                ),
            )

            val objects = repository.observeSpatialObjectsInGarden(gardenId).first()

            assertEquals(2, objects.size)
            assertEquals("Alpha path", objects[0].name)
            assertEquals("Zeta path", objects[1].name)
        }

    @Test
    fun point_geometry_round_trips() =
        runBlocking {
            val geometry = Geometry.Point(Coordinate(1.5, 2.5))
            val spatialObject = sampleSpatialObject().copy(geometry = geometry)
            repository.insert(spatialObject)

            assertEquals(geometry, repository.getSpatialObject(spatialObject.id)?.geometry)
        }

    @Test
    fun line_geometry_round_trips() =
        runBlocking {
            val geometry =
                Geometry.LineString(
                    listOf(Coordinate(0.0, 0.0), Coordinate(5.0, 0.0), Coordinate(5.0, 2.0)),
                )
            val spatialObject = sampleSpatialObject().copy(geometry = geometry)
            repository.insert(spatialObject)

            assertEquals(geometry, repository.getSpatialObject(spatialObject.id)?.geometry)
        }

    @Test
    fun polygon_geometry_round_trips() =
        runBlocking {
            val geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(2.0, 0.0),
                        Coordinate(2.0, 2.0),
                        Coordinate(0.0, 2.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            val spatialObject = sampleSpatialObject().copy(geometry = geometry)
            repository.insert(spatialObject)

            assertEquals(geometry, repository.getSpatialObject(spatialObject.id)?.geometry)
        }

    @Test
    fun null_name_round_trips_as_null() =
        runBlocking {
            val spatialObject = sampleSpatialObject().copy(name = null)
            repository.insert(spatialObject)

            assertNull(repository.getSpatialObject(spatialObject.id)?.name)
        }

    @Test
    fun null_area_id_round_trips_as_null() =
        runBlocking {
            val spatialObject = sampleSpatialObject().copy(areaId = null)
            repository.insert(spatialObject)

            assertNull(repository.getSpatialObject(spatialObject.id)?.areaId)
        }

    @Test
    fun object_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleSpatialObject().copy(objectType = InfrastructureType.TRELLIS))

            db.openHelper.readableDatabase
                .query("SELECT object_type FROM spatial_object")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("trellis", cursor.getString(0))
                }
        }

    @Test
    fun geometry_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(
                sampleSpatialObject().copy(
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
                .query("SELECT geometry_type, geometry_data FROM spatial_object")
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
    fun archive_sets_status_and_excludes_from_default_observation() =
        runBlocking {
            val spatialObject = sampleSpatialObject()
            repository.insert(spatialObject)

            repository.archive(spatialObject.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getSpatialObject(spatialObject.id)?.status,
            )
            assertEquals(
                0,
                repository.observeSpatialObjectsInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeSpatialObjectsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun restore_returns_the_object_to_active() =
        runBlocking {
            val spatialObject = sampleSpatialObject()
            repository.insert(spatialObject)
            repository.archive(spatialObject.id, 1_700_000_500_000L)

            repository.restore(spatialObject.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getSpatialObject(spatialObject.id)?.status,
            )
        }

    @Test
    fun archive_throws_NotFoundError_when_object_missing() =
        runBlocking {
            try {
                repository.archive("spatialobject_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("SpatialObject", expected.entityType)
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

    private fun sampleArea(): AreaEntity =
        AreaEntity(
            id = areaId,
            garden_id = gardenId,
            name = "Vegetable Garden",
            area_type = AreaType.ZONE,
            description = null,
            geometry_type = null,
            geometry_data = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleSpatialObject(): SpatialObject =
        SpatialObject(
            id = "spatialobject_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            objectType = InfrastructureType.PATH,
            geometry = Geometry.Point(Coordinate(0.0, 0.0)),
            status = RecordStatus.ACTIVE,
            name = "North path",
            description = "Maintenance access",
            areaId = areaId,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
