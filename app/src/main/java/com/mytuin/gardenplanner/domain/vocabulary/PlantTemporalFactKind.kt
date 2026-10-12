package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantTemporalFactKind.
 *
 * Discriminator for the plant_temporal_fact table (step 4c-B). Each
 * kind names one of the §41 temporal fields, expressed as a recurring
 * annual window.
 */
enum class PlantTemporalFactKind(
    override val id: String,
) : VocabularyValue {
    SOWING_PERIOD("sowing_period"),
    TRANSPLANTING_PERIOD("transplanting_period"),
    PLANTING_PERIOD("planting_period"),
    FLOWERING_PERIOD("flowering_period"),
    FRUITING_PERIOD("fruiting_period"),
    HARVEST_PERIOD("harvest_period"),
    ;

    companion object {
        fun fromId(id: String): PlantTemporalFactKind? = entries.firstOrNull { it.id == id }
    }
}
