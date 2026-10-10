package com.mytuin.gardenplanner.domain.vocabulary

/**
 * FrostSensitivity.
 *
 * PLANT_VOCABULARIES.md §12. Six values.
 *
 * Qualitative. Actual temperature thresholds remain structured data
 * (§37, no false precision).
 */
enum class FrostSensitivity(
    override val id: String,
) : VocabularyValue {
    VERY_SENSITIVE("very_sensitive"),
    SENSITIVE("sensitive"),
    MODERATELY_SENSITIVE("moderately_sensitive"),
    TOLERANT("tolerant"),
    VERY_TOLERANT("very_tolerant"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): FrostSensitivity? = entries.firstOrNull { it.id == id }
    }
}
