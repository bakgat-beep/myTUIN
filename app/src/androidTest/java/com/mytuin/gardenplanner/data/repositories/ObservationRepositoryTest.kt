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
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Observation
import com.mytuin.gardenplanner.domain.repository.ObservationRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ObservationRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: ObservationRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val areaId = "area_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val objectId = "spatialobject_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"
    private val observationId = "observation_00000000-0000-0000-0000-000000000001"
    private val activityId = "activity_00000000-0000-0000-0000-000000000001"

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
                ObservationRepositoryImpl(
                    db = db,
                    observationDao = db.observationDao(),
                    activityDao = db.activityDao(),
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
    fun getObservation_returns_null_when_no_observation_exists() =
        runBlocking {
            assertEquals(null, repository.getObservation("observation_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val observation = sampleObservation()
            repository.insert(observation)

            assertEquals(observation, repository.getObservation(observation.id))
        }

    @Test
    fun insert_writes_the_linked_activity() =
        runBlocking {
            repository.insert(sampleObservation())

            val activity = db.activityDao().getById(activityId)
            assertNotNull("linked activity must be created", activity)
            assertEquals(ActivityType.OBSERVATION, activity!!.activity_type)
            assertEquals(RecordStatus.ACTIVE, activity.status)
            assertEquals(sampleObservation().observedAt, activity.occurred_at)
            assertEquals(gardenId, activity.garden_id)
            assertEquals(areaId, activity.area_id)
            assertEquals(spaceId, activity.growing_space_id)
            assertEquals(objectId, activity.spatial_object_id)
            assertEquals(instanceId, activity.plant_instance_id)
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-0000000000bb",
                    activityId = "activity_00000000-0000-0000-0000-0000000000bb",
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
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-0000000000cc",
                    activityId = "activity_00000000-0000-0000-0000-0000000000cc",
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
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-0000000000dd",
                    activityId = "activity_00000000-0000-0000-0000-0000000000dd",
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
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-0000000000ee",
                    activityId = "activity_00000000-0000-0000-0000-0000000000ee",
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
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-0000000000ff",
                    activityId = "activity_00000000-0000-0000-0000-0000000000ff",
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
    fun observation_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleObservation().copy(observationType = ObservationType.SOIL))

            db.openHelper.readableDatabase
                .query("SELECT observation_type FROM observation")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("soil", cursor.getString(0))
                }
        }

    @Test
    fun confidence_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleObservation().copy(confidence = Confidence.MODERATE))

            db.openHelper.readableDatabase
                .query("SELECT confidence FROM observation")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("moderate", cursor.getString(0))
                }
        }

    @Test
    fun archive_cascades_to_the_linked_activity() =
        runBlocking {
            val observation = sampleObservation()
            repository.insert(observation)

            repository.archive(observation.id, 1_700_000_500_000L)

            val activity = db.activityDao().getById(activityId)
            assertEquals(RecordStatus.ARCHIVED, activity?.status)
        }

    @Test
    fun archived_observation_is_excluded_from_default_observation_query() =
        runBlocking {
            val observation = sampleObservation()
            repository.insert(observation)
            repository.archive(observation.id, 1_700_000_500_000L)

            assertEquals(
                0,
                repository.observeObservationsInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeObservationsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun observeObservationsInGarden_scopes_to_garden_and_excludes_archived() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000a1",
                    activityId = "activity_00000000-0000-0000-0000-00000000a1",
                ),
            )
            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000a2",
                    activityId = "activity_00000000-0000-0000-0000-00000000a2",
                ),
            )
            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000a3",
                    activityId = "activity_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                ),
            )
            repository.archive("observation_00000000-0000-0000-0000-00000000a2", 1_700_000_500_000L)

            assertEquals(
                1,
                repository.observeObservationsInGarden(gardenId).first().size,
            )
            assertEquals(
                2,
                repository.observeObservationsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun observeObservationsForPlantInstance_scopes_to_plant_instance() =
        runBlocking {
            val otherInstance = "plantinstance_00000000-0000-0000-0000-000000000002"
            db.plantInstanceDao().insert(
                samplePlantInstance().copy(id = otherInstance, name = "Other instance"),
            )

            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000b1",
                    activityId = "activity_00000000-0000-0000-0000-00000000b1",
                ),
            )
            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000b2",
                    activityId = "activity_00000000-0000-0000-0000-00000000b2",
                    plantInstanceId = otherInstance,
                ),
            )

            val observations = repository.observeObservationsForPlantInstance(instanceId).first()
            assertEquals(1, observations.size)
            assertEquals("observation_00000000-0000-0000-0000-00000000b1", observations.first().id)
        }

    @Test
    fun observeObservationsInGrowingSpace_scopes_to_growing_space() =
        runBlocking {
            val otherSpace = "growingspace_00000000-0000-0000-0000-000000000002"
            db.growingSpaceDao().insert(
                sampleGrowingSpace().copy(id = otherSpace, name = "Other Bed"),
            )

            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000c1",
                    activityId = "activity_00000000-0000-0000-0000-00000000c1",
                ),
            )
            repository.insert(
                sampleObservation().copy(
                    id = "observation_00000000-0000-0000-0000-00000000c2",
                    activityId = "activity_00000000-0000-0000-0000-00000000c2",
                    growingSpaceId = otherSpace,
                ),
            )

            val observations = repository.observeObservationsInGrowingSpace(spaceId).first()
            assertEquals(1, observations.size)
            assertEquals("observation_00000000-0000-0000-0000-00000000c1", observations.first().id)
        }

    @Test
    fun archive_throws_NotFoundError_when_observation_missing() =
        runBlocking {
            try {
                repository.archive("observation_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Observation", expected.entityType)
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

    private fun sampleObservation(): Observation =
        Observation(
            id = observationId,
            activityId = activityId,
            gardenId = gardenId,
            observedAt = 1_700_000_400_000L,
            observationType = ObservationType.PLANT,
            confidence = Confidence.HIGH,
            createdAt = 1_700_000_000_000L,
            areaId = areaId,
            growingSpaceId = spaceId,
            spatialObjectId = objectId,
            plantInstanceId = instanceId,
            structuredValues = null,
            notes = "Leaves are yellowing",
        )
}
