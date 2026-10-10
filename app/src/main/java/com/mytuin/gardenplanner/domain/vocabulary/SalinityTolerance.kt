package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SalinityTolerance.
 *
 * PLANT_VOCABULARIES.md §16. Six values.
 *
 * Actual salinity thresholds remain structured data.
 */
enum class SalinityTolerance(
    override val id: String,
) : VocabularyValue {
    VERY_SENSITIVE("very_sensitive"),
    SENSITIVE("sensitive"),
    MODERATE("moderate"),
    TOLERANT("tolerant"),
    VERY_TOLERANT("very_tolerant"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SalinityTolerance? = entries.firstOrNull { it.id == id }
    }
}
