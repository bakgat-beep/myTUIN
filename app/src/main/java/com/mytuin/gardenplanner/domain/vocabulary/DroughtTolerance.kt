package com.mytuin.gardenplanner.domain.vocabulary

/**
 * DroughtTolerance.
 *
 * PLANT_VOCABULARIES.md §14. Six values.
 *
 * Does not define irrigation requirements.
 */
enum class DroughtTolerance(
    override val id: String,
) : VocabularyValue {
    VERY_LOW("very_low"),
    LOW("low"),
    MODERATE("moderate"),
    HIGH("high"),
    VERY_HIGH("very_high"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): DroughtTolerance? = entries.firstOrNull { it.id == id }
    }
}
