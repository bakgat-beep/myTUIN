package com.mytuin.gardenplanner.domain.display

import com.mytuin.gardenplanner.domain.vocabulary.VocabularyValue

/**
 * Unit system.
 *
 * DEC-042: display/device preference. Stored in DataStore.
 *
 * DEC-042 also states: "Stored measurements remain in canonical units
 * regardless of the user's unit preference. Conversion to display
 * units happens at the presentation layer only. Changing the unit
 * preference must never alter a stored measurement." This enum
 * therefore affects formatting only; it never reaches the database.
 *
 * A185: id-carrying enum per DEC-040, for consistency.
 */
enum class UnitSystem(
    override val id: String,
) : VocabularyValue {
    METRIC("metric"),
    IMPERIAL("imperial"),
    ;

    companion object {
        val DEFAULT: UnitSystem = METRIC

        fun fromId(id: String): UnitSystem? = entries.firstOrNull { it.id == id }
    }
}
