package com.mytuin.gardenplanner.platform.time

import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * System clock backed by System.currentTimeMillis().
 *
 * The only production code in step 5b that reads wall-clock time.
 * Direct use of System.currentTimeMillis() elsewhere in application
 * code should be avoided; inject Clock instead.
 */
class SystemClock
    @Inject
    constructor() : Clock {
        override fun nowMillis(): Long = System.currentTimeMillis()
    }
