package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.time.Clock

/**
 * Fixed clock for tests.
 */
class FakeClock(
    var nowMillisValue: Long = 1_700_000_000_000L,
) : Clock {

    override fun nowMillis(): Long = nowMillisValue
}