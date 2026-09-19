package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Change the location of an existing Garden.
 *
 * PHASE_0_PROJECT_FOUNDATION §21, §30 item 18:
 *   "The user must be able to enter or change location manually."
 *
 * Enter exists via CreateGarden. Change is what this use case adds.
 * A124=a: garden location has no history table. DEC-041 names
 * growing space geometry and dimensions, and plant instance location,
 * but not garden location. A history table arrives only when a real
 * requirement exists.
 *
 * A119=a: scoped to location. Renaming or re-describing the garden
 * is a separate operation with no requirement yet.
 *
 * A123=a: the use case injects Clock and supplies updatedAt, keeping
 * the repository clock-free (matching UpdateGrowingSpaceGeometry).
 *
 * A127=a: a missing garden throws NotFoundError from the repository.
 */
class UpdateGardenLocation
    @Inject
    constructor(
        private val repository: GardenRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            gardenId: String,
            location: NewGardenLocation,
        ) {
            repository.updateLocation(
                id = gardenId,
                location = location,
                updatedAt = clock.nowMillis(),
            )
        }
    }
