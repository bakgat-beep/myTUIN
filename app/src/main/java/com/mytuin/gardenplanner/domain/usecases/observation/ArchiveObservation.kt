package com.mytuin.gardenplanner.domain.usecases.observation

import com.mytuin.gardenplanner.domain.repository.ObservationRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive an Observation.
 *
 * O5: the Observation has no status column. Archiving goes through
 * the linked Activity: ObservationRepository.archive archives the
 * Activity, which removes the Observation from default queries.
 *
 * The Observation row is preserved; only the Activity's status
 * changes.
 *
 * A missing Observation throws NotFoundError from the repository.
 */
class ArchiveObservation
    @Inject
    constructor(
        private val repository: ObservationRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(observationId: String) {
            repository.archive(id = observationId, archivedAt = clock.nowMillis())
        }
    }
