package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import javax.inject.Inject

/**
 * Mark one target of a Plan as completed.
 *
 * §29: partial completion is supported per target. "Beds 1-3
 * completed; Bed 4 not completed" is four rows in plan_target, three
 * with completed_at set and one without.
 *
 * This only marks the target. It does not create an Activity, and it
 * does not set the Plan's own status to `completed`. Both are the
 * caller's responsibility (Invariants 3, 4).
 */
class CompletePlanTarget
    @Inject
    constructor(
        private val repository: PlanRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            planId: String,
            targetType: PlanTargetType,
            targetId: String,
        ) {
            repository.completeTarget(
                planId = planId,
                targetType = targetType,
                targetId = targetId,
                completedAt = clock.nowMillis(),
            )
        }
    }
