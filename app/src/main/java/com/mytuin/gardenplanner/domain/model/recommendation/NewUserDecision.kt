package com.mytuin.gardenplanner.domain.model.recommendation

import com.mytuin.gardenplanner.domain.vocabulary.UserDecisionType

/**
 * User-settable fields for a new UserDecision.
 *
 * The use case assigns id and createdAt. `decidedAt` is
 * caller-supplied because the decision time is part of the record's
 * meaning, not its metadata.
 */
data class NewUserDecision(
    val gardenId: String,
    val decisionType: UserDecisionType,
    val decidedAt: Long,
    val recommendationId: String? = null,
    val planId: String? = null,
    val reason: String? = null,
    val notes: String? = null,
)
