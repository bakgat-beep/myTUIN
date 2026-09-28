package com.mytuin.gardenplanner.domain.model.garden

import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KnownDateTest {
    @Test
    fun date_only_constructs_with_null_time() {
        val knownDate = KnownDate(LocalDate.of(2026, 3, 15))

        assertEquals(LocalDate.of(2026, 3, 15), knownDate.date)
        assertEquals(null, knownDate.time)
        assertFalse(knownDate.isTimestamp)
    }

    @Test
    fun date_and_time_constructs_as_timestamp() {
        val knownDate =
            KnownDate(
                date = LocalDate.of(2026, 3, 15),
                time = LocalTime.of(9, 30),
            )

        assertEquals(LocalDate.of(2026, 3, 15), knownDate.date)
        assertEquals(LocalTime.of(9, 30), knownDate.time)
        assertTrue(knownDate.isTimestamp)
    }

    @Test
    fun isTimestamp_is_false_for_midnight_when_time_is_null() {
        // Midnight and "no time recorded" are different concepts.
        // This confirms that a date-only KnownDate is not accidentally
        // interpreted as midnight.
        val knownDate = KnownDate(LocalDate.of(2026, 3, 15))

        assertFalse(knownDate.isTimestamp)
    }
}
