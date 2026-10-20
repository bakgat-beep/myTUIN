package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.UserDecisionEntity
import com.mytuin.gardenplanner.domain.model.recommendation.UserDecision

/**
 * Mapping between UserDecisionEntity and UserDecision.
 *
 * Invariant (R6): at least one of recommendation_id and plan_id
 * must be non-null. Enforced at read time.
 */

fun UserDecisionEntity.toDomain(): UserDecision {
    require(recommendation_id != null || plan_id != null) {
        "UserDecision '$id' must reference a recommendation, a plan, or both; " +
            "got recommendation_id=$recommendation_id, plan_id=$plan_id"
    }
    return UserDecision(
        id = id,
        gardenId = garden_id,
        decisionType = decision_type,
        decidedAt = decided_at,
        createdAt = created_at,
        recommendationId = recommendation_id,
        planId = plan_id,
        reason = reason,
        notes = notes,
    )
}

fun UserDecision.toEntity(): UserDecisionEntity =
    UserDecisionEntity(
        id = id,
        garden_id = gardenId,
        decision_type = decisionType,
        decided_at = decidedAt,
        created_at = createdAt,
        recommendation_id = recommendationId,
        plan_id = planId,
        reason = reason,
        notes = notes,
    )
