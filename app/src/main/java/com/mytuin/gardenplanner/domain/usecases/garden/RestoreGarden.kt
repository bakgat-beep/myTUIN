package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived Garden.
 *
 * A232=a: restore is included in Phase 0 alongside archive.
 *
 * Sets status = ACTIVE. The pre-archive status is not preserved: a
 * garden that was DRAFT or INACTIVE before archiving is ACTIVE after
 * restore. Preserving the prior status would require a
 * pre_archive_status column, which is a schema change not made in
 * Phase 0 (A240). Recorded as a follow-up.
 */
class RestoreGarden
    @Inject
    constructor(
        private val repository: GardenRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(gardenId: String) {
            repository.restore(id = gardenId, restoredAt = clock.nowMillis())
        }
    }
