package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived SpatialObject.
 *
 * Sets status = ACTIVE. SpatialObject is created ACTIVE, so the
 * archive/restore round-trip is lossless for this entity, as it is
 * for GrowingSpace.
 */
class RestoreSpatialObject
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(spatialObjectId: String) {
            repository.restore(id = spatialObjectId, restoredAt = clock.nowMillis())
        }
    }
