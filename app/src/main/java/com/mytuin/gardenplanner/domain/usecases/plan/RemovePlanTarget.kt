package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import javax.inject.Inject

/**
 * Remove a target from a Plan.
 *
 * A missing target is not an error.
 */
class RemovePlanTarget
    @Inject
    constructor(
        private val repository: PlanRepository,
    ) {
        suspend operator fun invoke(
            planId: String,
            targetType: PlanTargetType,
            targetId: String,
        ) {
            repository.removeTarget(planId, targetType, targetId)
        }
    }
