package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived Plan.
 *
 * Sets recordStatus = ACTIVE. The Plan's PlanningStatus is untouched.
 */
class RestorePlan
    @Inject
    constructor(
        private val repository: PlanRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(planId: String) {
            repository.restore(id = planId, restoredAt = clock.nowMillis())
        }
    }
