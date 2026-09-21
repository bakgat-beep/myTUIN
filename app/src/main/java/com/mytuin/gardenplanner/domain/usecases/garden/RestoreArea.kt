package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived Area.
 *
 * Sets status = ACTIVE. Same pre-archive status caveat as
 * RestoreGarden (A240): a status of null before archiving becomes
 * ACTIVE after restore.
 */
class RestoreArea
    @Inject
    constructor(
        private val repository: AreaRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(areaId: String) {
            repository.restore(id = areaId, restoredAt = clock.nowMillis())
        }
    }
