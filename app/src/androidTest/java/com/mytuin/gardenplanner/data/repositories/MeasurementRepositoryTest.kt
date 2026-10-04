package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.Measurement
import com.mytuin.gardenplanner.domain.repository.MeasurementRepository
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementUnit
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
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
class MeasurementRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: MeasurementRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val areaId = "area_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val objectId = "spatialobject_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"

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
                MeasurementRepositoryImpl(
                    measurementDao = db.measurementDao(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                    spatialObjectDao = db.spatialObjectDao(),
                    plantInstanceDao = db.plantInstanceDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.areaDao().insert(sampleArea())
            db.growingSpaceDao().insert(sampleGrowingSpace())
            db.spatialObjectDao().insert(sampleSpatialObject())
            db.plantDao().insert(samplePlant())
            db.plantInstanceDao().insert(samplePlantInstance())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getMeasurement_returns_null_when_no_measurement_exists() =
        runBlocking {
            assertNull(repository.getMeasurement("measurement_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val measurement = sampleMeasurement()
            repository.insert(measurement)

            assertEquals(measurement, repository.getMeasurement(measurement.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-0000000000bb",
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
    fun insert_requires_existing_area_when_supplied() =
        runBlocking {
            val orphan =
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-0000000000cc",
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
    fun insert_requires_existing_growing_space_when_supplied() =
        runBlocking {
            val orphan =
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-0000000000dd",
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
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-0000000000ee",
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
    fun insert_requires_existing_plant_instance_when_supplied() =
        runBlocking {
            val orphan =
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-0000000000ff",
                    plantInstanceId = "plantinstance_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("plant_instance_id", expected.field)
            }
        }

    @Test
    fun measurement_property_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleMeasurement().copy(property = MeasurementProperty.TEMPERATURE))

            db.openHelper.readableDatabase
                .query("SELECT property FROM measurement")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("temperature", cursor.getString(0))
                }
        }

    @Test
    fun measurement_unit_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleMeasurement().copy(unit = MeasurementUnit.CELSIUS))

            db.openHelper.readableDatabase
                .query("SELECT unit FROM measurement")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("celsius", cursor.getString(0))
                }
        }

    @Test
    fun date_only_known_date_round_trips() =
        runBlocking {
            val measurement =
                sampleMeasurement().copy(
                    measuredAt = KnownDate(LocalDate.of(2026, 9, 15)),
                )
            repository.insert(measurement)

            val retrieved = repository.getMeasurement(measurement.id)!!.measuredAt
            assertEquals(LocalDate.of(2026, 9, 15), retrieved.date)
            assertNull(retrieved.time)
        }

    @Test
    fun timestamp_known_date_round_trips() =
        runBlocking {
            val measurement =
                sampleMeasurement().copy(
                    measuredAt =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(10, 30, 45),
                        ),
                )
            repository.insert(measurement)

            val retrieved = repository.getMeasurement(measurement.id)!!.measuredAt
            assertEquals(LocalDate.of(2026, 9, 15), retrieved.date)
            assertEquals(LocalTime.of(10, 30, 45), retrieved.time)
        }

    @Test
    fun numeric_value_precision_is_preserved() =
        runBlocking {
            repository.insert(sampleMeasurement().copy(value = 6.723456789))

            assertEquals(
                6.723456789,
                repository.getMeasurement("measurement_00000000-0000-0000-0000-000000000001")!!.value,
                1e-12,
            )
        }

    @Test
    fun observeMeasurementsInGarden_scopes_to_garden() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(sampleMeasurement().copy(id = "measurement_00000000-0000-0000-0000-00000000a1"))
            repository.insert(
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-00000000a2",
                    gardenId = otherGarden,
                ),
            )

            val measurements = repository.observeMeasurementsInGarden(gardenId).first()
            assertEquals(1, measurements.size)
            assertEquals("measurement_00000000-0000-0000-0000-00000000a1", measurements.first().id)
        }

    @Test
    fun observeMeasurementsForPlantInstance_scopes_to_plant_instance() =
        runBlocking {
            val otherInstance = "plantinstance_00000000-0000-0000-0000-000000000002"
            db.plantInstanceDao().insert(
                samplePlantInstance().copy(id = otherInstance, name = "Other instance"),
            )

            repository.insert(sampleMeasurement().copy(id = "measurement_00000000-0000-0000-0000-00000000b1"))
            repository.insert(
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-00000000b2",
                    plantInstanceId = otherInstance,
                ),
            )

            val measurements = repository.observeMeasurementsForPlantInstance(instanceId).first()
            assertEquals(1, measurements.size)
        }

    @Test
    fun observeMeasurementsInGrowingSpace_scopes_to_growing_space() =
        runBlocking {
            val otherSpace = "growingspace_00000000-0000-0000-0000-000000000002"
            db.growingSpaceDao().insert(
                sampleGrowingSpace().copy(id = otherSpace, name = "Other Bed"),
            )

            repository.insert(sampleMeasurement().copy(id = "measurement_00000000-0000-0000-0000-00000000c1"))
            repository.insert(
                sampleMeasurement().copy(
                    id = "measurement_00000000-0000-0000-0000-00000000c2",
                    growingSpaceId = otherSpace,
                ),
            )

            val measurements = repository.observeMeasurementsInGrowingSpace(spaceId).first()
            assertEquals(1, measurements.size)
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

    private fun sampleGrowingSpace(): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = spaceId,
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
            id = objectId,
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

    private fun samplePlantInstance(): PlantInstanceEntity =
        PlantInstanceEntity(
            id = instanceId,
            garden_id = gardenId,
            plant_id = plantId,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleMeasurement(): Measurement =
        Measurement(
            id = "measurement_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            property = MeasurementProperty.PH,
            value = 6.7,
            unit = MeasurementUnit.PH,
            measuredAt = KnownDate(LocalDate.of(2026, 9, 15)),
            confidence = Confidence.HIGH,
            createdAt = 1_700_000_000_000L,
            areaId = areaId,
            growingSpaceId = spaceId,
            spatialObjectId = objectId,
            plantInstanceId = instanceId,
            notes = "Home test kit",
        )
}
