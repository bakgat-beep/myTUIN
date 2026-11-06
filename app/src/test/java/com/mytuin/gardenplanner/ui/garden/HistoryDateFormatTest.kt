package com.mytuin.gardenplanner.ui.garden

import java.time.ZoneId
import java.util.Locale
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HistoryDateFormatTest {
    private val zone = ZoneId.of("UTC")
    private val locale = Locale.US
    private val now = 1_700_000_000_000L
    private val oneYearMillis = 31_536_000_000L

    private fun format(
        time: Long,
        minTime: Long,
        maxTime: Long,
    ): String = HistoryDateFormat.format(time, minTime, maxTime, locale, zone)

    @Test
    fun `year-range uses date-only format`() {
        val result = format(now, now - oneYearMillis * 2, now)
        // Contains hyphens from the date, no colons from the time
        assertTrue(result.contains("-"), result)
        assertTrue(!result.contains(":"), result)
    }

    @Test
    fun `day-range uses date-and-time format`() {
        val result = format(now, now - 86_400_000L * 30, now)
        assertTrue(result.contains("-"), result)
        assertTrue(result.contains(":"), result)
    }

    @Test
    fun `hour-range uses time-only format`() {
        val result = format(now, now - 3_600_000L * 5, now)
        assertTrue(!result.contains("-"), result)
        assertTrue(result.contains(":"), result)
    }

    @Test
    fun `known UTC time formats as expected`() {
        // 2023-11-14 22:13:20 UTC
        val at = now
        val result = format(at, at - oneYearMillis * 2, at)
        assertEquals("2023-11-14", result)
    }
}
