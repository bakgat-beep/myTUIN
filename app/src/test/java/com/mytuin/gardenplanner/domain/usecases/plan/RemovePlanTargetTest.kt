package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.testdoubles.FakePlanRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RemovePlanTargetTest {
    private val repository = FakePlanRepository()
    private val useCase = RemovePlanTarget(repository)

    private val planId = "plan_test_0001"

    @Test
    fun invoke_removes_the_target() =
        runBlocking {
            repository.addTarget(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001", null)
            assertEquals(1, repository.targetsSnapshot().size)

            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001")

            assertEquals(0, repository.targetsSnapshot().size)
        }

    @Test
    fun invoke_on_missing_target_is_not_an_error() =
        runBlocking {
            useCase(planId, PlanTargetType.GROWING_SPACE, "growingspace_test_0001")
            assertEquals(0, repository.targetsSnapshot().size)
        }
}
