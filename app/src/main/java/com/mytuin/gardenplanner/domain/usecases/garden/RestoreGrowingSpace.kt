package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived GrowingSpace.
 *
 * Sets status = ACTIVE. GrowingSpace is created ACTIVE (step 5c), so
 * the archive/restore round-trip is lossless for this entity.
 */
class RestoreGrowingSpace
    @Inject
    constructor(
        private val repository: GrowingSpaceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(growingSpaceId: String) {
            repository.restore(id = growingSpaceId, restoredAt = clock.nowMillis())
        }
    }
