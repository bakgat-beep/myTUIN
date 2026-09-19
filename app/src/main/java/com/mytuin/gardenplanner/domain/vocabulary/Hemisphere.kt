package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Hemisphere.
 *
 * CORE_VOCABULARIES.md §8. Values are stable canonical identifiers.
 * The enum constant name is a code-level convenience and MUST NOT be
 * used as a stored value (DEC-040).
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
enum class Hemisphere(
    override val id: String,
) : VocabularyValue {
    NORTHERN("northern"),
    SOUTHERN("southern"),
    EQUATORIAL("equatorial"),
    GLOBAL("global"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): Hemisphere? = entries.firstOrNull { it.id == id }
    }
}
