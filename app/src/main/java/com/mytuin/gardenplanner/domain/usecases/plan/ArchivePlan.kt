package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a Plan.
 *
 * Sets recordStatus = ARCHIVED. The Plan's planning lifecycle status
 * (PlanningStatus) is untouched (PL5).
 */
class ArchivePlan
    @Inject
    constructor(
        private val repository: PlanRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(planId: String) {
            repository.archive(id = planId, archivedAt = clock.nowMillis())
        }
    }
