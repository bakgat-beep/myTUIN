package com.mytuin.gardenplanner.domain.usecases.soil

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewSoilRecord
import com.mytuin.gardenplanner.domain.model.garden.SoilRecord
import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new SoilRecord.
 *
 * Assigns id, status (ACTIVE), createdAt, updatedAt.
 *
 * The use case does not itself enforce the at-most-one-of
 * (areaId, growingSpaceId) invariant; it passes the input through.
 * The mapper rejects a row with both set on read, so a caller that
 * supplies both gets a repository-level failure when the row is
 * read back. If you prefer the failure at insert time, that is a
 * small additional check to add here; not done in Step 5 to keep
 * the use case aligned with the repository-as-validator pattern.
 *
 * Returns the new SoilRecord's id.
 */
class CreateSoilRecord
    @Inject
    constructor(
        private val repository: SoilRecordRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewSoilRecord): String {
            val now = clock.nowMillis()
            val record =
                SoilRecord(
                    id = idGenerator.newSoilRecordId(),
                    gardenId = input.gardenId,
                    status = RecordStatus.ACTIVE,
                    createdAt = now,
                    updatedAt = now,
                    areaId = input.areaId,
                    growingSpaceId = input.growingSpaceId,
                    name = input.name,
                    notes = input.notes,
                )
            repository.insert(record)
            return record.id
        }
    }
