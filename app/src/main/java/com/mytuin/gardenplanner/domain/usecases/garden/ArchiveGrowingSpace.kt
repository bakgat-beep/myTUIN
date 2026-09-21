package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a GrowingSpace.
 *
 * Same semantics as ArchiveGarden. The space's current row is
 * preserved; its growing_space_history rows remain linked. Archiving
 * a space does not archive the garden it belongs to, and vice versa
 * (A236=a).
 */
class ArchiveGrowingSpace
    @Inject
    constructor(
        private val repository: GrowingSpaceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(growingSpaceId: String) {
            repository.archive(id = growingSpaceId, archivedAt = clock.nowMillis())
        }
    }
