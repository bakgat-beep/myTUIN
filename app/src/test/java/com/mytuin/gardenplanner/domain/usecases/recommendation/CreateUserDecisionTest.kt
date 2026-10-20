package com.mytuin.gardenplanner.domain.usecases.recommendation

import com.mytuin.gardenplanner.domain.model.recommendation.NewUserDecision
import com.mytuin.gardenplanner.domain.vocabulary.UserDecisionType
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeUserDecisionRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreateUserDecisionTest {
    private val repository = FakeUserDecisionRepository()
    private val idGenerator = FakeIdGenerator(nextUserDecisionIdValue = "userdecision_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createUserDecision =
        CreateUserDecision(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createUserDecision(minimalInput())
            assertEquals("userdecision_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_and_createdAt() =
        runBlocking {
            createUserDecision(minimalInput())

            val decisions = repository.snapshot()
            assertEquals(1, decisions.size)
            val decision = decisions.first()

            assertEquals("userdecision_test_0001", decision.id)
            assertEquals("garden_test_0001", decision.gardenId)
            assertEquals(UserDecisionType.ACCEPTED, decision.decisionType)
            assertEquals(1_700_000_500_000L, decision.decidedAt)
            assertEquals(1_700_000_000_000L, decision.createdAt)
        }

    @Test
    fun invoke_defaults_optional_fields_to_null() =
        runBlocking {
            createUserDecision(minimalInput())
            val decision = repository.snapshot().first()

            assertNull(decision.planId)
            assertNull(decision.reason)
            assertNull(decision.notes)
        }

    @Test
    fun invoke_preserves_recommendation_and_plan_ids() =
        runBlocking {
            createUserDecision(
                minimalInput().copy(
                    recommendationId = "recommendation_test_0001",
                    planId = "plan_test_0001",
                ),
            )
            val decision = repository.snapshot().first()

            assertEquals("recommendation_test_0001", decision.recommendationId)
            assertEquals("plan_test_0001", decision.planId)
        }

    @Test
    fun invoke_preserves_reason_and_notes() =
        runBlocking {
            createUserDecision(
                minimalInput().copy(
                    reason = "Chose a different variety",
                    notes = "Decided at the nursery",
                ),
            )
            val decision = repository.snapshot().first()

            assertEquals("Chose a different variety", decision.reason)
            assertEquals("Decided at the nursery", decision.notes)
        }

    private fun minimalInput(): NewUserDecision =
        NewUserDecision(
            gardenId = "garden_test_0001",
            decisionType = UserDecisionType.ACCEPTED,
            decidedAt = 1_700_000_500_000L,
            recommendationId = "recommendation_test_0001",
        )
}
