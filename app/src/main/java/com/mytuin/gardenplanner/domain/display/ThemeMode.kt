package com.mytuin.gardenplanner.domain.display

import com.mytuin.gardenplanner.domain.vocabulary.VocabularyValue

/**
 * Theme mode.
 *
 * DEC-042: a display/device preference, stored in DataStore, not Room.
 * Not exported. Not part of the garden schema.
 *
 * A185: follows the DEC-040 pattern with an explicit id, even though
 * the value never leaves the device. Consistency matters more than
 * the exception; a future decision to export display preferences
 * would not require a storage format change.
 *
 * A186: display concepts live in domain/display/ rather than
 * domain/vocabulary/, because they are not shared across the garden
 * domain. They are still VocabularyValue-shaped for consistency.
 */
enum class ThemeMode(override val id: String) : VocabularyValue {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark"),
    ;

    companion object {
        val DEFAULT: ThemeMode = SYSTEM

        fun fromId(id: String): ThemeMode? =
            entries.firstOrNull { it.id == id }
    }
}