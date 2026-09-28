package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived PlantInstance.
 *
 * Sets status = ACTIVE. The `lifecycle` field is not touched:
 * restoring a record does not change the plant's lifecycle state.
 */
class RestorePlantInstance
    @Inject
    constructor(
        private val repository: PlantInstanceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(plantInstanceId: String) {
            repository.restore(id = plantInstanceId, restoredAt = clock.nowMillis())
        }
    }
