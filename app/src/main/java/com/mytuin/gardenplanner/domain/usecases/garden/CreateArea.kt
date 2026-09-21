package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.model.garden.NewArea
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new Area.
 *
 * Assigns id, status (ACTIVE), createdAt, updatedAt. Returns the new
 * id. Mirrors CreateGrowingSpace.
 *
 * The garden_id FK is enforced by Room. A missing Garden surfaces as
 * ValidationError(field = "garden_id") from the repository.
 */
class CreateArea
    @Inject
    constructor(
        private val repository: AreaRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewArea): String {
            val now = clock.nowMillis()
            val area =
                Area(
                    id = idGenerator.newAreaId(),
                    gardenId = input.gardenId,
                    name = input.name,
                    areaType = input.areaType,
                    description = input.description,
                    geometry = input.geometry,
                    status = RecordStatus.ACTIVE,
                    createdAt = now,
                    updatedAt = now,
                )
            repository.insert(area)
            return area.id
        }
    }
