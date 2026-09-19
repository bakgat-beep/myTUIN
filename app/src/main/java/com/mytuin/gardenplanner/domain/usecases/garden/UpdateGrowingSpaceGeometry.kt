package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Change the geometry of an existing GrowingSpace.
 *
 * DEC-041: the repository writes the prior state to the history table
 * and updates the current row in a single transaction. The use case
 * supplies effectiveAt via Clock (A70=a) and a free-text reason
 * (A69=a).
 *
 * Only geometry is changed (A66=a). Dimensions arrive in a later step.
 */
class UpdateGrowingSpaceGeometry
    @Inject
    constructor(
        private val repository: GrowingSpaceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            growingSpaceId: String,
            newGeometry: Geometry?,
            reason: String? = null,
        ) {
            repository.updateGeometry(
                id = growingSpaceId,
                newGeometry = newGeometry,
                effectiveAt = clock.nowMillis(),
                reason = reason,
            )
        }
    }
