package com.mytuin.gardenplanner.domain.usecases.soil

import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived SoilRecord.
 *
 * Sets status = ACTIVE and updated_at.
 */
class RestoreSoilRecord
    @Inject
    constructor(
        private val repository: SoilRecordRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(soilRecordId: String) {
            repository.restore(id = soilRecordId, restoredAt = clock.nowMillis())
        }
    }
