package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import javax.inject.Inject

/**
 * Change a Plan's planning lifecycle status.
 *
 * §27, §28, Invariants 3 and 4: a Plan reaching `completed` does not
 * itself prove that an activity occurred. The user records the
 * actual activity through CreateActivity, optionally with the Plan's
 * id (PL9).
 */
class UpdatePlanStatus
    @Inject
    constructor(
        private val repository: PlanRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            planId: String,
            status: PlanningStatus,
        ) {
            repository.updateStatus(
                id = planId,
                status = status,
                updatedAt = clock.nowMillis(),
            )
        }
    }
