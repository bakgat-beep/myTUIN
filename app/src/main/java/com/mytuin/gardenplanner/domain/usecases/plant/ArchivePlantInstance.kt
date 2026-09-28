package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a PlantInstance.
 *
 * Mirrors ArchiveGarden and ArchiveArea. Sets status = ARCHIVED and
 * updated_at. The row is preserved.
 *
 * A missing PlantInstance throws NotFoundError from the repository.
 */
class ArchivePlantInstance
    @Inject
    constructor(
        private val repository: PlantInstanceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(plantInstanceId: String) {
            repository.archive(id = plantInstanceId, archivedAt = clock.nowMillis())
        }
    }
