package com.mytuin.gardenplanner.domain.usecases.harvest

import com.mytuin.gardenplanner.domain.repository.HarvestRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a Harvest.
 *
 * HL11: the Harvest has no status column. Archiving goes through the
 * linked Activity: HarvestRepository.archive archives the Activity,
 * which removes the Harvest from default queries.
 *
 * The Harvest row is preserved; only the Activity's status changes.
 */
class ArchiveHarvest
    @Inject
    constructor(
        private val repository: HarvestRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(harvestId: String) {
            repository.archive(id = harvestId, archivedAt = clock.nowMillis())
        }
    }
