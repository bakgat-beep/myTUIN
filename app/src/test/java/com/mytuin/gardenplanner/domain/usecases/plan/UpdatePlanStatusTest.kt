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

class UpdatePlanStatusTest {
    private val repository = FakePlanRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = UpdatePlanStatus(repository, clock)

    private val planId = "plan_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Plan(
                    id = planId,
                    gardenId = "garden_test_0001",
                    planType = ActivityType.WATERING,
                    status = PlanningStatus.PLANNED,
                    recordStatus = RecordStatus.ACTIVE,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
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
    fun invoke_updates_the_status() =
        runBlocking {
            useCase(planId, PlanningStatus.SCHEDULED)
            assertEquals(PlanningStatus.SCHEDULED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_updatedAt_from_clock() =
        runBlocking {
            useCase(planId, PlanningStatus.SCHEDULED)
            assertEquals(1_700_000_500_000L, repository.snapshot().single().updatedAt)
        }

    @Test
    fun invoke_does_not_change_record_status() =
        runBlocking {
            useCase(planId, PlanningStatus.COMPLETED)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().recordStatus)
        }
}
