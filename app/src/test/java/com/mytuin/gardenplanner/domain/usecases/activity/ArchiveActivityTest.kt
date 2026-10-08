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

class ArchiveActivityTest {
    private val repository = FakeActivityRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = ArchiveActivity(repository, clock)

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
                    status = RecordStatus.ACTIVE,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_archived() =
        runBlocking {
            useCase(activityId)
            assertEquals(RecordStatus.ARCHIVED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_archivedAt_from_clock() =
        runBlocking {
            useCase(activityId)
            assertEquals(
                RecordStatus.ARCHIVED,
                repository.statusChangeCalls().single().status,
            )
            assertEquals(1_700_000_500_000L, repository.statusChangeCalls().single().at)
        }

    @Test
    fun invoke_does_not_change_occurredAt_or_createdAt() =
        runBlocking {
            useCase(activityId)
            val activity = repository.snapshot().single()
            assertEquals(1_700_000_400_000L, activity.occurredAt)
            assertEquals(1_700_000_000_000L, activity.createdAt)
        }
}
