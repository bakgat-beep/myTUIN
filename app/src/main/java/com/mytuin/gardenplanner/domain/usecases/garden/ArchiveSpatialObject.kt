package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a SpatialObject.
 *
 * Mirrors ArchiveArea. Sets status = ARCHIVED and updated_at. The
 * row is preserved.
 *
 * A missing SpatialObject throws NotFoundError from the repository.
 */
class ArchiveSpatialObject
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(spatialObjectId: String) {
            repository.archive(id = spatialObjectId, archivedAt = clock.nowMillis())
        }
    }
