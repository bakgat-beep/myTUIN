package com.mytuin.gardenplanner.domain.model.garden

import java.time.LocalDate
import java.time.LocalTime

/**
 * A date that may or may not have a known time of day.
 *
 * V1_DATABASE_SCHEMA §7: "Where an event has a meaningful time:
 * store a timestamp. Where only a calendar date is known: store a
 * date without inventing a time. The application must not fabricate
 * precision."
 *
 * `time == null` means "known to the day".
 * `time != null` means "known to the minute" (or finer, storage
 * permitting).
 *
 * Storage: see PlantInstanceMapper. Each KnownDate field on an
 * entity is stored as two nullable columns (epoch day + millis since
 * midnight), not one column with a precision flag. The mapper
 * enforces the pairing.
 *
 * Uses java.time.LocalDate and LocalTime. minSdk is 26, which
 * supports these directly. These are JDK types, not Android types,
 * so the domain layer boundary is preserved.
 */
data class KnownDate(
    val date: LocalDate,
    val time: LocalTime? = null,
) {
    val isTimestamp: Boolean get() = time != null
}
