package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ShadeTolerance.
 *
 * PLANT_VOCABULARIES.md §18. Six values.
 *
 * A plant may prefer full sun while still having some shade
 * tolerance. Distinct from LightRequirement (§17).
 */
enum class ShadeTolerance(
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
        fun fromId(id: String): ShadeTolerance? = entries.firstOrNull { it.id == id }
    }
}
