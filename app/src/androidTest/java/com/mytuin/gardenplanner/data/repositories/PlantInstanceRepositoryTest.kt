package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import java.time.LocalDate
import java.time.LocalTime
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
class PlantInstanceRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: PlantInstanceRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val cultivarId = "cultivar_00000000-0000-0000-0000-000000000001"
    private val growingSpaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val spatialObjectId = "spatialobject_00000000-0000-0000-0000-000000000001"

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
                PlantInstanceRepositoryImpl(
                    db = db,
                    plantInstanceDao = db.plantInstanceDao(),
                    plantInstanceHistoryDao = db.plantInstanceHistoryDao(),
                    idGenerator =
                        com.mytuin.gardenplanner.platform.identifiers
                            .UuidIdGenerator(),
                    gardenDao = db.gardenDao(),
                    plantDao = db.plantDao(),
                    cultivarDao = db.cultivarDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                    spatialObjectDao = db.spatialObjectDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.plantDao().insert(samplePlant())
            db.cultivarDao().insert(sampleCultivar())
            db.growingSpaceDao().insert(sampleGrowingSpace())
            db.spatialObjectDao().insert(sampleSpatialObject())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getPlantInstance_returns_null_when_no_instance_exists() =
        runBlocking {
            assertNull(repository.getPlantInstance("plantinstance_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val instance = sampleInstance()
            repository.insert(instance)

            assertEquals(instance, repository.getPlantInstance(instance.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000bb",
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
    fun insert_requires_existing_plant() =
        runBlocking {
            val orphan =
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000cc",
                    plantId = "plant_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("plant_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_cultivar_when_supplied() =
        runBlocking {
            val orphan =
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000dd",
                    cultivarId = "cultivar_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("cultivar_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_growing_space_when_supplied() =
        runBlocking {
            val orphan =
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000ee",
                    growingSpaceId = "growingspace_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("growing_space_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_spatial_object_when_supplied() =
        runBlocking {
            val orphan =
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000ff",
                    spatialObjectId = "spatialobject_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("spatial_object_id", expected.field)
            }
        }

    @Test
    fun observePlantInstancesInGarden_scopes_to_garden_and_excludes_archived() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000a1",
                ),
            )
            repository.insert(
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000a2",
                ),
            )
            repository.insert(
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                ),
            )
            repository.archive(
                "plantinstance_00000000-0000-0000-0000-00000000a2",
                1_700_000_500_000L,
            )

            val active = repository.observePlantInstancesInGarden(gardenId).first()
            assertEquals(1, active.size)

            val all = repository.observePlantInstancesInGarden(gardenId, includeArchived = true).first()
            assertEquals(2, all.size)
        }

    @Test
    fun observePlantInstancesInGrowingSpace_scopes_by_growing_space() =
        runBlocking {
            val otherSpace = "growingspace_00000000-0000-0000-0000-000000000002"
            db.growingSpaceDao().insert(
                sampleGrowingSpace().copy(id = otherSpace, name = "Other Bed"),
            )

            repository.insert(
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000b1",
                ),
            )
            repository.insert(
                sampleInstance().copy(
                    id = "plantinstance_00000000-0000-0000-0000-00000000b2",
                    growingSpaceId = otherSpace,
                ),
            )

            val inSpace = repository.observePlantInstancesInGrowingSpace(growingSpaceId).first()
            assertEquals(1, inSpace.size)
            assertEquals(
                "plantinstance_00000000-0000-0000-0000-00000000b1",
                inSpace.first().id,
            )
        }

    @Test
    fun date_only_known_date_round_trips() =
        runBlocking {
            val instance =
                sampleInstance().copy(
                    plannedDate = KnownDate(LocalDate.of(2026, 9, 15)),
                )
            repository.insert(instance)

            val retrieved = repository.getPlantInstance(instance.id)?.plannedDate
            assertEquals(LocalDate.of(2026, 9, 15), retrieved?.date)
            assertNull(retrieved?.time)
        }

    @Test
    fun timestamp_known_date_round_trips() =
        runBlocking {
            val instance =
                sampleInstance().copy(
                    plantedDate =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(9, 30, 45),
                        ),
                )
            repository.insert(instance)

            val retrieved = repository.getPlantInstance(instance.id)?.plantedDate
            assertEquals(LocalDate.of(2026, 9, 15), retrieved?.date)
            assertEquals(LocalTime.of(9, 30, 45), retrieved?.time)
        }

    @Test
    fun all_four_date_fields_round_trip_independently() =
        runBlocking {
            val instance =
                sampleInstance().copy(
                    plannedDate = KnownDate(LocalDate.of(2026, 3, 1)),
                    plantedDate = KnownDate(LocalDate.of(2026, 3, 15), LocalTime.of(9, 0)),
                    expectedEndDate = KnownDate(LocalDate.of(2026, 8, 1)),
                    removedDate = KnownDate(LocalDate.of(2026, 9, 1), LocalTime.of(17, 30)),
                )
            repository.insert(instance)

            val retrieved = repository.getPlantInstance(instance.id)!!
            assertEquals(LocalDate.of(2026, 3, 1), retrieved.plannedDate?.date)
            assertEquals(LocalDate.of(2026, 3, 15), retrieved.plantedDate?.date)
            assertEquals(LocalTime.of(9, 0), retrieved.plantedDate?.time)
            assertEquals(LocalDate.of(2026, 8, 1), retrieved.expectedEndDate?.date)
            assertEquals(LocalDate.of(2026, 9, 1), retrieved.removedDate?.date)
            assertEquals(LocalTime.of(17, 30), retrieved.removedDate?.time)
        }

    @Test
    fun planting_stock_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleInstance().copy(plantingStock = PlantingStockType.SEEDLING))

            db.openHelper.readableDatabase
                .query("SELECT planting_stock FROM plant_instance")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("seedling", cursor.getString(0))
                }
        }

    @Test
    fun lifecycle_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(
                sampleInstance().copy(lifecycle = PlantInstanceLifecycle.ESTABLISHED),
            )

            db.openHelper.readableDatabase
                .query("SELECT lifecycle FROM plant_instance")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("established", cursor.getString(0))
                }
        }

    @Test
    fun point_geometry_round_trips() =
        runBlocking {
            val geometry = Geometry.Point(Coordinate(2.5, 3.5))
            val instance = sampleInstance().copy(geometry = geometry)
            repository.insert(instance)

            assertEquals(geometry, repository.getPlantInstance(instance.id)?.geometry)
        }

    @Test
    fun line_geometry_round_trips() =
        runBlocking {
            val geometry =
                Geometry.LineString(
                    listOf(Coordinate(0.0, 0.0), Coordinate(4.0, 0.0)),
                )
            val instance = sampleInstance().copy(geometry = geometry)
            repository.insert(instance)

            assertEquals(geometry, repository.getPlantInstance(instance.id)?.geometry)
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
            val instance = sampleInstance().copy(geometry = geometry)
            repository.insert(instance)

            assertEquals(geometry, repository.getPlantInstance(instance.id)?.geometry)
        }

    @Test
    fun null_geometry_round_trips_as_null() =
        runBlocking {
            val instance = sampleInstance().copy(geometry = null)
            repository.insert(instance)

            assertNull(repository.getPlantInstance(instance.id)?.geometry)
        }

    @Test
    fun archive_sets_status_and_excludes_from_default_observation() =
        runBlocking {
            val instance = sampleInstance()
            repository.insert(instance)

            repository.archive(instance.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getPlantInstance(instance.id)?.status,
            )
            assertEquals(
                0,
                repository.observePlantInstancesInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observePlantInstancesInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun restore_returns_the_instance_to_active() =
        runBlocking {
            val instance = sampleInstance()
            repository.insert(instance)
            repository.archive(instance.id, 1_700_000_500_000L)

            repository.restore(instance.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getPlantInstance(instance.id)?.status,
            )
        }

    @Test
    fun archive_throws_NotFoundError_when_instance_missing() =
        runBlocking {
            try {
                repository.archive("plantinstance_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("PlantInstance", expected.entityType)
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

    private fun samplePlant(): PlantEntity =
        PlantEntity(
            id = plantId,
            canonical_name = "Test plant",
            scientific_name = null,
            genus = null,
            species = null,
            family = null,
            lifecycle = PlantLifecycle.PERENNIAL,
            description = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleCultivar(): CultivarEntity =
        CultivarEntity(
            id = cultivarId,
            plant_id = plantId,
            name = "Test cultivar",
            description = null,
            notes = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleGrowingSpace(): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = growingSpaceId,
            garden_id = gardenId,
            name = "Bed 2",
            space_type = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            geometry_type = null,
            geometry_data = null,
            length = null,
            width = null,
            height = null,
            diameter = null,
            area = null,
            volume = null,
            description = null,
            area_id = null,
            notes = null,
        )

    private fun sampleSpatialObject(): SpatialObjectEntity =
        SpatialObjectEntity(
            id = spatialObjectId,
            garden_id = gardenId,
            object_type = InfrastructureType.PATH,
            geometry_type = GeometryType.POINT,
            geometry_data = """{"type":"Point","coordinates":[0.0,0.0]}""",
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            name = "North path",
            description = null,
            area_id = null,
            notes = null,
        )

    private fun sampleInstance(): PlantInstance =
        PlantInstance(
            id = "plantinstance_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            plantId = plantId,
            status = RecordStatus.ACTIVE,
            cultivarId = cultivarId,
            growingSpaceId = growingSpaceId,
            spatialObjectId = spatialObjectId,
            name = "Tomato in Bed 2",
            quantity = 3,
            plannedDate = null,
            plantedDate = null,
            expectedEndDate = null,
            removedDate = null,
            plantingStock = null,
            lifecycle = null,
            geometry = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
