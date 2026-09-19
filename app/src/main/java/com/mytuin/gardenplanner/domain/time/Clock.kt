package com.mytuin.gardenplanner.domain.time

/**
 * Time source.
 *
 * V1_TECHNICAL_ARCHITECTURE §63 lists date/time handling among the
 * core shared concepts. V1_DATABASE_SCHEMA §7 requires that timestamps
 * preserve the actual recorded time without fabricating precision.
 *
 * Interface so tests can inject a fixed time. The production
 * implementation lives in platform/time/.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface Clock {
    fun nowMillis(): Long
}
