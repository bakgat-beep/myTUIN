package com.mytuin.gardenplanner.domain.usecases.activity

import com.mytuin.gardenplanner.domain.repository.ActivityRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived Activity.
 *
 * Sets status = ACTIVE. All other fields are untouched.
 */
class RestoreActivity
    @Inject
    constructor(
        private val repository: ActivityRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(activityId: String) {
            repository.restore(id = activityId, restoredAt = clock.nowMillis())
        }
    }
