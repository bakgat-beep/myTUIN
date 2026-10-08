package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakePlanRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CompletePlanTargetTest {
    private val repository = FakePlanRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = CompletePlanTarget(repository, clock)

    private val planId = "plan_test_0001"

    @Test
    fun invoke_sets_completedAt_from_clock() =
        runBlocking {
            repository.addTarget(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001", null)

            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001")

            assertEquals(
                1_700_000_500_000L,
                repository.targetsSnapshot().single().completedAt,
            )
        }

    @Test
    fun partial_completion_across_targets_is_supported() =
        runBlocking {
            // §29 example: Beds 1-3 completed; Bed 4 not.
            repository.addTarget(planId, PlanTargetType.GROWING_SPACE, "space_1", null)
            repository.addTarget(planId, PlanTargetType.GROWING_SPACE, "space_2", null)
            repository.addTarget(planId, PlanTargetType.GROWING_SPACE, "space_3", null)
            repository.addTarget(planId, PlanTargetType.GROWING_SPACE, "space_4", null)

            useCase(planId, PlanTargetType.GROWING_SPACE, "space_1")
            useCase(planId, PlanTargetType.GROWING_SPACE, "space_2")
            useCase(planId, PlanTargetType.GROWING_SPACE, "space_3")

            val completed =
                repository.targetsSnapshot().count { it.completedAt != null }
            val pending =
                repository.targetsSnapshot().count { it.completedAt == null }

            assertEquals(3, completed)
            assertEquals(1, pending)
            assertNull(
                repository.targetsSnapshot().first { it.targetId == "space_4" }.completedAt,
            )
        }
}
