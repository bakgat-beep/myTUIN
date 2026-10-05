package com.mytuin.gardenplanner.domain.usecases.harvest

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Harvest
import com.mytuin.gardenplanner.domain.model.garden.NewHarvest
import com.mytuin.gardenplanner.domain.repository.HarvestRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Record a new Harvest.
 *
 * HL1: generates two ids — one for the Harvest, one for the linked
 * harvesting Activity — and passes a fully-formed Harvest to the
 * repository, which writes both rows in one transaction.
 *
 * The Activity's content is derived from the Harvest:
 *   activity_type = harvesting
 *   occurred_at   = harvest date, normalized per HarvestMapper
 *   location refs = plant_instance_id and growing_space_id only
 *   quantity/unit = the Harvest's
 *   status        = ACTIVE
 *   notes         = null (the note lives on the Harvest)
 *
 * Returns the new Harvest's id.
 */
class CreateHarvest
    @Inject
    constructor(
        private val repository: HarvestRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewHarvest): String {
            val harvest =
                Harvest(
                    id = idGenerator.newHarvestId(),
                    activityId = idGenerator.newActivityId(),
                    gardenId = input.gardenId,
                    date = input.date,
                    quantity = input.quantity,
                    unit = input.unit,
                    createdAt = clock.nowMillis(),
                    sizeCategory = input.sizeCategory,
                    plantInstanceId = input.plantInstanceId,
                    growingSpaceId = input.growingSpaceId,
                    notes = input.notes,
                )
            repository.insert(harvest)
            return harvest.id
        }
    }
