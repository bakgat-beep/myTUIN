package com.mytuin.gardenplanner.domain.vocabulary

/**
 * WaterRequirement.
 *
 * PLANT_VOCABULARIES §41 `water_requirement`. No dedicated section in
 * PLANT_VOCABULARIES, so the value set follows the pattern of the
 * other bounded qualitative scales (§14 drought tolerance, §15
 * waterlogging tolerance, §18 shade tolerance).
 *
 * A broad planning classification, not a measured value. Actual
 * watering guidance belongs to structured knowledge and activity
 * records.
 */
enum class WaterRequirement(
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
        fun fromId(id: String): WaterRequirement? = entries.firstOrNull { it.id == id }
    }
}
