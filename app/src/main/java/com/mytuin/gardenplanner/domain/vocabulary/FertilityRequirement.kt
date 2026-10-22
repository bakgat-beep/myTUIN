package com.mytuin.gardenplanner.domain.vocabulary

/**
 * FertilityRequirement.
 *
 * PLANT_VOCABULARIES §41 `fertility_requirement`. No dedicated
 * section, so the value set follows the pattern of the other
 * bounded qualitative scales (§14 drought tolerance, §15
 * waterlogging tolerance).
 *
 * A broad planning classification, not a nutrient measurement.
 */
enum class FertilityRequirement(
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
        fun fromId(id: String): FertilityRequirement? = entries.firstOrNull { it.id == id }
    }
}
