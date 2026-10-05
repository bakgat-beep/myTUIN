package com.mytuin.gardenplanner.domain.usecases.harvest

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.HarvestLoss
import com.mytuin.gardenplanner.domain.model.garden.NewHarvestLoss
import com.mytuin.gardenplanner.domain.repository.HarvestLossRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Record a new HarvestLoss.
 *
 * Assigns id and createdAt. Does not create an Activity: a loss is
 * standalone by default (HL2 (b), HL12). When the caller supplies an
 * activityId, the repository verifies the referenced Activity exists
 * but does not modify it.
 *
 * Returns the new id.
 */
class CreateHarvestLoss
    @Inject
    constructor(
        private val repository: HarvestLossRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewHarvestLoss): String {
            val harvestLoss =
                HarvestLoss(
                    id = idGenerator.newHarvestLossId(),
                    gardenId = input.gardenId,
                    date = input.date,
                    quantity = input.quantity,
                    unit = input.unit,
                    createdAt = clock.nowMillis(),
                    activityId = input.activityId,
                    cause = input.cause,
                    severity = input.severity,
                    plantInstanceId = input.plantInstanceId,
                    growingSpaceId = input.growingSpaceId,
                    notes = input.notes,
                )
            repository.insert(harvestLoss)
            return harvestLoss.id
        }
    }
