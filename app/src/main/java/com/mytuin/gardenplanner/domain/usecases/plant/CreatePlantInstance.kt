package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstance
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new PlantInstance.
 *
 * Assigns id, status (ACTIVE), createdAt, updatedAt. Returns the
 * new id.
 *
 * All five foreign keys (garden, plant, cultivar, growing space,
 * spatial object) are enforced by Room. Missing parents surface as
 * ValidationError with the offending field named.
 */
class CreatePlantInstance
    @Inject
    constructor(
        private val repository: PlantInstanceRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewPlantInstance): String {
            val now = clock.nowMillis()
            val plantInstance =
                PlantInstance(
                    id = idGenerator.newPlantInstanceId(),
                    gardenId = input.gardenId,
                    plantId = input.plantId,
                    status = RecordStatus.ACTIVE,
                    cultivarId = input.cultivarId,
                    growingSpaceId = input.growingSpaceId,
                    spatialObjectId = input.spatialObjectId,
                    name = input.name,
                    quantity = input.quantity,
                    plannedDate = input.plannedDate,
                    plantedDate = input.plantedDate,
                    expectedEndDate = input.expectedEndDate,
                    removedDate = input.removedDate,
                    plantingStock = input.plantingStock,
                    lifecycle = input.lifecycle,
                    geometry = input.geometry,
                    notes = input.notes,
                    createdAt = now,
                    updatedAt = now,
                )
            repository.insert(plantInstance)
            return plantInstance.id
        }
    }
