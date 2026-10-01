package com.mytuin.gardenplanner.domain.usecases.activity

import com.mytuin.gardenplanner.domain.model.garden.ActivityDetail
import com.mytuin.gardenplanner.domain.model.garden.NewActivity
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin
import com.mytuin.gardenplanner.domain.vocabulary.PlantingMethod
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.WateringMethod
import com.mytuin.gardenplanner.testdoubles.FakeActivityRepository
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateActivity.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateActivityTest {
    private val repository = FakeActivityRepository()
    private val idGenerator = FakeIdGenerator(nextActivityIdValue = "activity_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createActivity =
        CreateActivity(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createActivity(minimalInput())
            assertEquals("activity_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createActivity(minimalInput())

            val activities = repository.snapshot()
            assertEquals(1, activities.size)
            val activity = activities.first()

            assertEquals("activity_test_0001", activity.id)
            assertEquals("garden_test_0001", activity.gardenId)
            assertEquals(ActivityType.WATERING, activity.activityType)
            assertEquals(RecordStatus.ACTIVE, activity.status)
            assertEquals(1_700_000_000_000L, activity.createdAt)
            assertEquals(1_700_000_500_000L, activity.occurredAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createActivity(minimalInput())
            val activity = repository.snapshot().first()

            assertNull(activity.areaId)
            assertNull(activity.growingSpaceId)
            assertNull(activity.spatialObjectId)
            assertNull(activity.plantInstanceId)
            assertNull(activity.quantity)
            assertNull(activity.unit)
            assertNull(activity.dataOrigin)
            assertNull(activity.notes)
        }

    @Test
    fun invoke_preserves_all_five_optional_parent_references() =
        runBlocking {
            createActivity(
                minimalInput().copy(
                    areaId = "area_test_0001",
                    growingSpaceId = "growingspace_test_0001",
                    spatialObjectId = "spatialobject_test_0001",
                    plantInstanceId = "plantinstance_test_0001",
                ),
            )
            val activity = repository.snapshot().first()

            assertEquals("area_test_0001", activity.areaId)
            assertEquals("growingspace_test_0001", activity.growingSpaceId)
            assertEquals("spatialobject_test_0001", activity.spatialObjectId)
            assertEquals("plantinstance_test_0001", activity.plantInstanceId)
        }

    @Test
    fun invoke_preserves_quantity_and_unit() =
        runBlocking {
            createActivity(
                minimalInput().copy(
                    quantity = 20.0,
                    unit = ActivityQuantityUnit.LITRE,
                ),
            )
            val activity = repository.snapshot().first()

            assertEquals(20.0, activity.quantity!!, 0.0)
            assertEquals(ActivityQuantityUnit.LITRE, activity.unit)
        }

    @Test
    fun invoke_preserves_data_origin_and_notes() =
        runBlocking {
            createActivity(
                minimalInput().copy(
                    dataOrigin = DataOrigin.USER_OBSERVED,
                    notes = "Watered along the south edge",
                ),
            )
            val activity = repository.snapshot().first()

            assertEquals(DataOrigin.USER_OBSERVED, activity.dataOrigin)
            assertEquals("Watered along the south edge", activity.notes)
        }

    @Test
    fun invoke_preserves_watering_detail() =
        runBlocking {
            createActivity(
                minimalInput().copy(
                    detail = ActivityDetail.Watering(WateringMethod.WATERING_CAN),
                ),
            )
            val activity = repository.snapshot().first()

            assertEquals(
                ActivityDetail.Watering(WateringMethod.WATERING_CAN),
                activity.detail,
            )
        }

    @Test
    fun invoke_preserves_planting_detail() =
        runBlocking {
            createActivity(
                minimalInput().copy(
                    activityType = ActivityType.PLANTING,
                    detail = ActivityDetail.Planting(PlantingMethod.TRANSPLANT),
                ),
            )
            val activity = repository.snapshot().first()

            assertEquals(
                ActivityDetail.Planting(PlantingMethod.TRANSPLANT),
                activity.detail,
            )
        }

    @Test
    fun invoke_defaults_detail_to_plain() =
        runBlocking {
            createActivity(minimalInput())
            assertEquals(ActivityDetail.Plain, repository.snapshot().first().detail)
        }

    @Test
    fun invoke_accepts_all_eleven_activity_types() =
        runBlocking {
            ActivityType.entries.forEach { type ->
                val detail =
                    when (type) {
                        ActivityType.PLANTING -> ActivityDetail.Planting(null)
                        ActivityType.WATERING -> ActivityDetail.Watering(null)
                        ActivityType.FEEDING -> ActivityDetail.Feeding(null)
                        ActivityType.PRUNING -> ActivityDetail.Pruning(null)
                        ActivityType.SOIL_WORK -> ActivityDetail.SoilWork(null)
                        else -> ActivityDetail.Plain
                    }
                createActivity(minimalInput().copy(activityType = type, detail = detail))
            }
            assertEquals(11, repository.snapshot().size)
            assertEquals(
                ActivityType.entries.toSet(),
                repository.snapshot().map { it.activityType }.toSet(),
            )
        }

    private fun minimalInput(): NewActivity =
        NewActivity(
            gardenId = "garden_test_0001",
            activityType = ActivityType.WATERING,
            occurredAt = 1_700_000_500_000L,
        )
}
