package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantMultiValueKind.
 *
 * Discriminator for the plant_multi_value join table.
 *
 * Step 4c-B added WATER_REQUIREMENT, PREFERRED_SOIL and SOIL_DRAINAGE.
 * Step 4e adds ROTATION_GROUP (P41E-2 (b)): the value_id for that
 * kind is a RotationGroup id, not a vocabulary id. The mapper passes
 * it through unchanged.
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
    ;

    companion object {
        fun fromId(id: String): PlantMultiValueKind? = entries.firstOrNull { it.id == id }
    }
}
