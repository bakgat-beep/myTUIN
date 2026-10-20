package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.recommendation.UserDecision
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for UserDecision.
 *
 * A first-class entity of its own; can reference a Recommendation, a
 * Plan, or both (R8). Not archived (R7): a UserDecision is an
 * immutable event.
 */
interface UserDecisionRepository {
    fun observeUserDecisionsInGarden(gardenId: String): Flow<List<UserDecision>>

    fun observeUserDecisionsForRecommendation(recommendationId: String): Flow<List<UserDecision>>

    fun observeUserDecisionsForPlan(planId: String): Flow<List<UserDecision>>

    fun observeUserDecision(id: String): Flow<UserDecision?>

    suspend fun getUserDecision(id: String): UserDecision?

    suspend fun insert(decision: UserDecision)
}
