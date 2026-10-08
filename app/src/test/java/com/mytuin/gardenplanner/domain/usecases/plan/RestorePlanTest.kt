package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakePlanRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestorePlanTest {
    private val repository = FakePlanRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestorePlan(repository, clock)

    private val planId = "plan_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Plan(
                    id = planId,
                    gardenId = "garden_test_0001",
                    planType = ActivityType.WATERING,
                    status = PlanningStatus.COMPLETED,
                    recordStatus = RecordStatus.ARCHIVED,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                    plannedStart = null,
                    plannedEnd = null,
                    quantity = null,
                    unit = null,
                    priority = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_sets_record_status_to_active() =
        runBlocking {
            useCase(planId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().recordStatus)
        }

    @Test
    fun invoke_does_not_change_planning_status() =
        runBlocking {
            useCase(planId)
            assertEquals(PlanningStatus.COMPLETED, repository.snapshot().single().status)
        }
}
