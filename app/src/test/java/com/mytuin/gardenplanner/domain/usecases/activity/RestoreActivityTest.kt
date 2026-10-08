package com.mytuin.gardenplanner.domain.usecases.activity

import com.mytuin.gardenplanner.domain.model.garden.Activity
import com.mytuin.gardenplanner.domain.model.garden.ActivityDetail
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeActivityRepository
import com.mytuin.gardenplanner.testdoubles.FakeClock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreActivityTest {
    private val repository = FakeActivityRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreActivity(repository, clock)

    private val activityId = "activity_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Activity(
                    id = activityId,
                    gardenId = "garden_test_0001",
                    activityType = ActivityType.WATERING,
                    occurredAt = 1_700_000_400_000L,
                    createdAt = 1_700_000_000_000L,
                    areaId = null,
                    growingSpaceId = null,
                    spatialObjectId = null,
                    plantInstanceId = null,
                    planId = null,
                    quantity = null,
                    unit = null,
                    detail = ActivityDetail.Plain,
                    dataOrigin = null,
                    status = RecordStatus.ARCHIVED,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(activityId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_restoredAt_from_clock() =
        runBlocking {
            useCase(activityId)
            assertEquals(1_700_001_000_000L, repository.statusChangeCalls().single().at)
        }
}
