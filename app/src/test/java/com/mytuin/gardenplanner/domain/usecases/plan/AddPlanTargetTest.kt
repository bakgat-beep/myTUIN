package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.testdoubles.FakePlanRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AddPlanTargetTest {
    private val repository = FakePlanRepository()
    private val useCase = AddPlanTarget(repository)

    private val planId = "plan_test_0001"

    @Test
    fun invoke_creates_a_target() =
        runBlocking {
            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001")

            val target = repository.targetsSnapshot().single()
            assertEquals(planId, target.planId)
            assertEquals(PlanTargetType.GROWING_SPACE, target.targetType)
            assertEquals("growingspace_test_0001", target.targetId)
            assertNull(target.completedAt)
            assertNull(target.notes)
        }

    @Test
    fun invoke_preserves_notes() =
        runBlocking {
            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001", "North edge")

            assertEquals("North edge", repository.targetsSnapshot().single().notes)
        }

    @Test
    fun adding_the_same_target_twice_replaces_notes() =
        runBlocking {
            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001", "First")
            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001", "Second")

            val targets = repository.targetsSnapshot()
            assertEquals(1, targets.size)
            assertEquals("Second", targets.single().notes)
        }
}
