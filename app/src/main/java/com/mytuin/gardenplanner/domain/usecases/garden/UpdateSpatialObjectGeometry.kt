package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Change the geometry of an existing SpatialObject (Step 9g).
 *
 * DEC-041 / DEC-051: the repository writes the prior state to the
 * history table and updates the current row in a single
 * transaction. The use case supplies effectiveAt via Clock and a
 * free-text reason (which 9g passes as "Edited on map" from the map
 * editor; the value is stored verbatim, no interpretation here).
 *
 * newGeometry is non-null because SpatialObject.geometry is
 * required (A5). This differs from UpdateGrowingSpaceGeometry where
 * the geometry parameter is nullable because growing-space geometry
 * is optional.
 */
class UpdateSpatialObjectGeometry
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            spatialObjectId: String,
            newGeometry: Geometry,
            reason: String? = null,
        ) {
            repository.updateGeometry(
                id = spatialObjectId,
                newGeometry = newGeometry,
                effectiveAt = clock.nowMillis(),
                reason = reason,
            )
        }
    }
