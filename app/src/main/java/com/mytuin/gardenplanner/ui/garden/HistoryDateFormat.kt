package com.mytuin.gardenplanner.ui.garden

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Formatting rules for the history slider's current-time label
 * (Step 9i, Q7: adaptive).
 *
 *   Range > 1 year   → "yyyy-MM-dd"
 *   Range > 1 day    → "yyyy-MM-dd HH:mm"
 *   Range ≤ 1 day    → "HH:mm:ss"
 *
 * The locale is a parameter so tests can pin it; production uses the
 * device default.
 */
object HistoryDateFormat {
    private const val MILLIS_PER_DAY = 86_400_000L
    private const val MILLIS_PER_YEAR = 31_536_000_000L

    private const val PATTERN_YEAR_RANGE = "yyyy-MM-dd"
    private const val PATTERN_DAY_RANGE = "yyyy-MM-dd HH:mm"
    private const val PATTERN_HOUR_RANGE = "HH:mm:ss"

    fun format(
        time: Long,
        minTime: Long,
        maxTime: Long,
        locale: Locale = Locale.getDefault(),
        zone: ZoneId = ZoneId.systemDefault(),
    ): String {
        val range = maxTime - minTime
        val pattern =
            when {
                range > MILLIS_PER_YEAR -> PATTERN_YEAR_RANGE
                range > MILLIS_PER_DAY -> PATTERN_DAY_RANGE
                else -> PATTERN_HOUR_RANGE
            }
        return Instant
            .ofEpochMilli(time)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern(pattern, locale))
    }
}
