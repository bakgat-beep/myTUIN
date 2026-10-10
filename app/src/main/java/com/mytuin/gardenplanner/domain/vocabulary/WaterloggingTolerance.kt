package com.mytuin.gardenplanner.domain.vocabulary

/**
 * WaterloggingTolerance.
 *
 * PLANT_VOCABULARIES.md §15. Six values.
 *
 * Does not describe soil drainage itself. Soil drainage is a soil
 * classification, not a plant classification.
 */
enum class WaterloggingTolerance(
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
        fun fromId(id: String): WaterloggingTolerance? = entries.firstOrNull { it.id == id }
    }
}
