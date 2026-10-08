package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewPlan
import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new Plan.
 *
 * Assigns id, recordStatus (ACTIVE), createdAt and updatedAt.
 * `status` is taken from the input, defaulting to IDEA.
 *
 * Returns the new Plan's id. No targets are created here; the
 * caller adds them with AddPlanTarget.
 */
class CreatePlan
    @Inject
    constructor(
        private val repository: PlanRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewPlan): String {
            val now = clock.nowMillis()
            val plan =
                Plan(
                    id = idGenerator.newPlanId(),
                    gardenId = input.gardenId,
                    planType = input.planType,
                    status = input.status,
                    recordStatus = RecordStatus.ACTIVE,
                    createdAt = now,
                    updatedAt = now,
                    plannedStart = input.plannedStart,
                    plannedEnd = input.plannedEnd,
                    quantity = input.quantity,
                    unit = input.unit,
                    priority = input.priority,
                    notes = input.notes,
                )
            repository.insert(plan)
            return plan.id
        }
    }
