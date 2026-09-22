package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewSpatialObject
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new SpatialObject.
 *
 * Assigns id, status (ACTIVE), createdAt, updatedAt. Returns the new
 * id. Mirrors CreateArea and CreateGrowingSpace.
 *
 * The garden_id and area_id foreign keys are enforced by Room. A
 * missing Garden or Area surfaces as ValidationError from the
 * repository with the offending field named.
 */
class CreateSpatialObject
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewSpatialObject): String {
            val now = clock.nowMillis()
            val spatialObject =
                SpatialObject(
                    id = idGenerator.newSpatialObjectId(),
                    gardenId = input.gardenId,
                    objectType = input.objectType,
                    geometry = input.geometry,
                    status = RecordStatus.ACTIVE,
                    name = input.name,
                    description = input.description,
                    areaId = input.areaId,
                    notes = input.notes,
                    createdAt = now,
                    updatedAt = now,
                )
            repository.insert(spatialObject)
            return spatialObject.id
        }
    }
