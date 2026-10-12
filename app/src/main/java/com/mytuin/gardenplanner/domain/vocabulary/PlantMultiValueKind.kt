package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantMultiValueKind.
 *
 * Discriminator for the plant_multi_value join table (P41B-5).
 *
 * Step 4c-B adds three kinds: WATER_REQUIREMENT, PREFERRED_SOIL and
 * SOIL_DRAINAGE, filling §41 fields that fit the multi-value shape
 * (P41C-2).
 */
enum class PlantMultiValueKind(
    override val id: String,
) : VocabularyValue {
    PLANT_ROLE("plant_role"),
    FOOD_PRODUCTION_CATEGORY("food_production_category"),
    GROWTH_HABIT("growth_habit"),
    ROOT_BEHAVIOUR("root_behaviour"),
    GROWING_METHOD("growing_method"),
    PROPAGATION_METHOD("propagation_method"),
    PLANTING_STOCK("planting_stock"),
    WATER_REQUIREMENT("water_requirement"),
    PREFERRED_SOIL("preferred_soil"),
    SOIL_DRAINAGE("soil_drainage"),
    ;

    companion object {
        fun fromId(id: String): PlantMultiValueKind? = entries.firstOrNull { it.id == id }
    }
}
