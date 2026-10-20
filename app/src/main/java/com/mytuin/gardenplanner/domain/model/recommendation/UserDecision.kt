package com.mytuin.gardenplanner.domain.model.recommendation

import com.mytuin.gardenplanner.domain.vocabulary.UserDecisionType

/**
 * UserDecision — domain model.
 *
 * V1_DATABASE_SCHEMA §46. DATA_MODEL §50.
 *
 * The gardener's response to a recommendation or proposed action.
 * Immutable event; not archived (R7). A wrong decision is corrected
 * by recording a new one.
 *
 * At least one of recommendationId and planId must be non-null
 * (R6). Both may be set (a decision that accepts a recommendation
 * and creates a plan in one action).
 */
data class UserDecision(
    val id: String,
    val gardenId: String,
    val decisionType: UserDecisionType,
    val decidedAt: Long,
    val createdAt: Long,
    val recommendationId: String?,
    val planId: String?,
    val reason: String?,
    val notes: String?,
)
