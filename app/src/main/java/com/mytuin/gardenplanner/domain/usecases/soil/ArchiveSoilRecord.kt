package com.mytuin.gardenplanner.domain.usecases.soil

import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a SoilRecord.
 *
 * Sets status = ARCHIVED and updated_at. The row is preserved.
 */
class ArchiveSoilRecord
    @Inject
    constructor(
        private val repository: SoilRecordRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(soilRecordId: String) {
            repository.archive(id = soilRecordId, archivedAt = clock.nowMillis())
        }
    }
