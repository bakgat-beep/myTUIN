package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantMultiValueKind.
 *
 * Discriminator for the plant_multi_value join table.
 *
 * Step 4c-B added WATER_REQUIREMENT, PREFERRED_SOIL and SOIL_DRAINAGE.
 * Step 4e added ROTATION_GROUP.
 * Step 8 added TOXIC_PART, PRESERVATION_METHOD and SUPPORT_TYPE,
 * closing the §41 multi-value deferrals.
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
    ROTATION_GROUP("rotation_group"),
    TOXIC_PART("toxic_part"),
    PRESERVATION_METHOD("preservation_method"),
    SUPPORT_TYPE("support_type"),
    ;

    companion object {
        fun fromId(id: String): PlantMultiValueKind? = entries.firstOrNull { it.id == id }
    }
}
