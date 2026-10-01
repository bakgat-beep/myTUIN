package com.mytuin.gardenplanner.domain.usecases.activity

import com.mytuin.gardenplanner.domain.repository.ActivityRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive an Activity.
 *
 * Sets status = ARCHIVED. The row is preserved.
 *
 * Archiving is used sparingly for Activity. §8 of the schema prefers
 * correction or supersession over deletion for historical records;
 * an Activity that was recorded in error would normally be corrected
 * by an append-only correction record (a later step). Archiving is
 * reserved for the case where the record should be hidden from
 * ordinary views without being destroyed.
 *
 * A missing Activity throws NotFoundError from the repository.
 */
class ArchiveActivity
    @Inject
    constructor(
        private val repository: ActivityRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(activityId: String) {
            repository.archive(id = activityId, archivedAt = clock.nowMillis())
        }
    }
