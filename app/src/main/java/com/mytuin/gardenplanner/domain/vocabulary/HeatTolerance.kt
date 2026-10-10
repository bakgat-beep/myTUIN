package com.mytuin.gardenplanner.domain.vocabulary

/**
 * HeatTolerance.
 *
 * PLANT_VOCABULARIES.md §13. Six values.
 *
 * Actual temperature requirements and limits remain structured data.
 */
enum class HeatTolerance(
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
        fun fromId(id: String): HeatTolerance? = entries.firstOrNull { it.id == id }
    }
}
