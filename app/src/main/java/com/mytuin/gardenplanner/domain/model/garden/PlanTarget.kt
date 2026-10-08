package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType

/**
 * A target of a Plan.
 *
 * V1_DATABASE_SCHEMA §41. A Plan may have zero or more targets. A
 * target is (type, id) rather than a foreign key, because the id
 * points into one of several tables depending on its type (PL2 (a)).
 *
 * `completedAt` is per-target (PL10). It directly supports §29's
 * partial-completion requirement: Beds 1-3 completed, Bed 4 not.
 *
 * Identity is (planId, targetType, targetId). Not a standalone
 * entity.
 */
data class PlanTarget(
    val planId: String,
    val targetType: PlanTargetType,
    val targetId: String,
    val completedAt: Long?,
    val notes: String?,
)
