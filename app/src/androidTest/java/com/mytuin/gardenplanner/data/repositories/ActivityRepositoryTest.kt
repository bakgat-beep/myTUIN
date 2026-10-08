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
import com.mytuin.gardenplanner.data.entities.PlanEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Activity
import com.mytuin.gardenplanner.domain.model.garden.ActivityDetail
import com.mytuin.gardenplanner.domain.repository.ActivityRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PruningMethod
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.WateringMethod
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
class ActivityRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: ActivityRepository

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
                ActivityRepositoryImpl(
                    activityDao = db.activityDao(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                    spatialObjectDao = db.spatialObjectDao(),
                    plantInstanceDao = db.plantInstanceDao(),
                    planDao = db.planDao(),
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
    fun getActivity_returns_null_when_no_activity_exists() =
        runBlocking {
            assertNull(repository.getActivity("activity_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val activity = sampleActivity()
            repository.insert(activity)

            assertEquals(activity, repository.getActivity(activity.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-0000000000bb",
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
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-0000000000cc",
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
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-0000000000dd",
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
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-0000000000ee",
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
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-0000000000ff",
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
    fun watering_method_round_trips() =
        runBlocking {
            val activity =
                sampleActivity().copy(
                    activityType = ActivityType.WATERING,
                    detail = ActivityDetail.Watering(WateringMethod.HOSE),
                )
            repository.insert(activity)

            assertEquals(activity, repository.getActivity(activity.id))
        }

    @Test
    fun activity_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(
                sampleActivity().copy(
                    activityType = ActivityType.SOIL_WORK,
                    detail = ActivityDetail.SoilWork(null),
                ),
            )

            db.openHelper.readableDatabase
                .query("SELECT activity_type FROM activity")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("soil_work", cursor.getString(0))
                }
        }

    @Test
    fun watering_method_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(
                sampleActivity().copy(
                    activityType = ActivityType.WATERING,
                    detail = ActivityDetail.Watering(WateringMethod.DRIP),
                ),
            )

            db.openHelper.readableDatabase
                .query("SELECT watering_method FROM activity")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("drip", cursor.getString(0))
                }
        }

    @Test
    fun quantity_and_unit_round_trip() =
        runBlocking {
            val activity =
                sampleActivity().copy(
                    quantity = 12.5,
                    unit = ActivityQuantityUnit.KILOGRAM,
                )
            repository.insert(activity)

            val retrieved = repository.getActivity(activity.id)!!
            assertEquals(12.5, retrieved.quantity!!, 0.0)
            assertEquals(ActivityQuantityUnit.KILOGRAM, retrieved.unit)
        }

    @Test
    fun quantity_and_unit_both_null_round_trip() =
        runBlocking {
            val activity = sampleActivity().copy(quantity = null, unit = null)
            repository.insert(activity)

            val retrieved = repository.getActivity(activity.id)!!
            assertNull(retrieved.quantity)
            assertNull(retrieved.unit)
        }

    @Test
    fun data_origin_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleActivity().copy(dataOrigin = DataOrigin.USER_OBSERVED))

            db.openHelper.readableDatabase
                .query("SELECT data_origin FROM activity")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("user_observed", cursor.getString(0))
                }
        }

    @Test
    fun activity_type_and_detail_must_be_consistent() =
        runBlocking {
            // A pruning activity with a watering detail is a
            // data-integrity error; the mapper rejects it.
            val malformed =
                sampleActivity().copy(
                    activityType = ActivityType.PRUNING,
                    detail = ActivityDetail.Watering(WateringMethod.HOSE),
                )
            try {
                repository.insert(malformed)
                fail("Expected IllegalArgumentException; insert succeeded")
            } catch (expected: IllegalArgumentException) {
                // expected
            }
        }

    @Test
    fun activity_type_plain_cannot_carry_subtype_detail() =
        runBlocking {
            val malformed =
                sampleActivity().copy(
                    activityType = ActivityType.HARVESTING,
                    detail = ActivityDetail.Pruning(PruningMethod.SHAPING),
                )
            try {
                repository.insert(malformed)
                fail("Expected IllegalArgumentException; insert succeeded")
            } catch (expected: IllegalArgumentException) {
                // expected
            }
        }

    @Test
    fun observeActivitiesInGarden_scopes_and_excludes_archived() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(sampleActivity().copy(id = "activity_00000000-0000-0000-0000-00000000a1"))
            repository.insert(sampleActivity().copy(id = "activity_00000000-0000-0000-0000-00000000a2"))
            repository.insert(
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                ),
            )
            repository.archive("activity_00000000-0000-0000-0000-00000000a2", 1_700_000_500_000L)

            assertEquals(
                1,
                repository.observeActivitiesInGarden(gardenId).first().size,
            )
            assertEquals(
                2,
                repository.observeActivitiesInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun observeActivitiesForPlantInstance_scopes_to_plant_instance() =
        runBlocking {
            val otherInstance = "plantinstance_00000000-0000-0000-0000-000000000002"
            db.plantInstanceDao().insert(
                samplePlantInstance().copy(id = otherInstance, name = "Other instance"),
            )

            repository.insert(sampleActivity().copy(id = "activity_00000000-0000-0000-0000-00000000b1"))
            repository.insert(
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-00000000b2",
                    plantInstanceId = otherInstance,
                ),
            )

            val activities = repository.observeActivitiesForPlantInstance(instanceId).first()
            assertEquals(1, activities.size)
            assertEquals("activity_00000000-0000-0000-0000-00000000b1", activities.first().id)
        }

    @Test
    fun plan_id_round_trips_when_supplied() =
        runBlocking {
            val plan = samplePlan()
            db.planDao().insert(plan)

            val activity = sampleActivity().copy(planId = plan.id)
            repository.insert(activity)

            assertEquals(plan.id, repository.getActivity(activity.id)?.planId)
        }

    @Test
    fun observeActivitiesForPlan_scopes_to_plan() =
        runBlocking {
            val plan = samplePlan()
            db.planDao().insert(plan)

            repository.insert(
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-00000000d1",
                    planId = plan.id,
                ),
            )
            repository.insert(
                sampleActivity().copy(
                    id = "activity_00000000-0000-0000-0000-00000000d2",
                    planId = null,
                ),
            )

            val linked = repository.observeActivitiesForPlan(plan.id).first()
            assertEquals(1, linked.size)
            assertEquals("activity_00000000-0000-0000-0000-00000000d1", linked.first().id)
        }

    @Test
    fun archive_sets_status_and_excludes_from_default_observation() =
        runBlocking {
            val activity = sampleActivity()
            repository.insert(activity)

            repository.archive(activity.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getActivity(activity.id)?.status,
            )
        }

    @Test
    fun restore_returns_the_activity_to_active() =
        runBlocking {
            val activity = sampleActivity()
            repository.insert(activity)
            repository.archive(activity.id, 1_700_000_500_000L)

            repository.restore(activity.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getActivity(activity.id)?.status,
            )
        }

    @Test
    fun archive_throws_NotFoundError_when_activity_missing() =
        runBlocking {
            try {
                repository.archive("activity_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Activity", expected.entityType)
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

    private fun sampleActivity(): Activity =
        Activity(
            id = "activity_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            activityType = ActivityType.WATERING,
            occurredAt = 1_700_000_400_000L,
            createdAt = 1_700_000_000_000L,
            areaId = areaId,
            growingSpaceId = spaceId,
            spatialObjectId = objectId,
            plantInstanceId = instanceId,
            planId = null,
            quantity = 5.0,
            unit = ActivityQuantityUnit.LITRE,
            detail = ActivityDetail.Watering(WateringMethod.WATERING_CAN),
            dataOrigin = DataOrigin.USER_OBSERVED,
            status = RecordStatus.ACTIVE,
            notes = "Watered the tomatoes",
        )

    private fun samplePlan(): PlanEntity =
        PlanEntity(
            id = "plan_00000000-0000-0000-0000-000000000001",
            garden_id = gardenId,
            plan_type = ActivityType.WATERING,
            status = PlanningStatus.PLANNED,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            record_status = RecordStatus.ACTIVE,
        )
}
