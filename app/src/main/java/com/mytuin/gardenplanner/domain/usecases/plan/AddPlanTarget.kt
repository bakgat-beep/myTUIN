package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import javax.inject.Inject

/**
 * Add a target to a Plan.
 *
 * §41: a Plan may have multiple targets. Each target is (type, id).
 * The repository pre-checks that both the Plan and the target entity
 * exist (PL2 (a)).
 *
 * Re-adding the same target is an upsert: the prior notes are
 * replaced, completed_at is preserved.
 */
class AddPlanTarget
    @Inject
    constructor(
        private val repository: PlanRepository,
    ) {
        suspend operator fun invoke(
            planId: String,
            targetType: PlanTargetType,
            targetId: String,
            notes: String? = null,
        ) {
            repository.addTarget(planId, targetType, targetId, notes)
        }
    }
