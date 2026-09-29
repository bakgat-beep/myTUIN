package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstanceLocation
import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Change the location of an existing PlantInstance.
 *
 * DEC-041: the repository writes the prior location to the history
 * table and updates the current row in a single transaction. The use
 * case supplies effectiveAt via Clock and a free-text reason.
 *
 * Only location is changed (H2, H6). Status, lifecycle, dates,
 * planting stock and notes are out of scope for this use case.
 */
class UpdatePlantInstanceLocation
    @Inject
    constructor(
        private val repository: PlantInstanceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            plantInstanceId: String,
            location: NewPlantInstanceLocation,
            reason: String? = null,
        ) {
            repository.updateLocation(
                id = plantInstanceId,
                location = location,
                effectiveAt = clock.nowMillis(),
                reason = reason,
            )
        }
    }
