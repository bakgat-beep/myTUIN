package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantMultiValueKind.
 *
 * Discriminator for the plant_multi_value join table (P41B-5). Each
 * kind names the vocabulary whose canonical ids populate the
 * `value_id` column for rows of that kind.
 *
 * Adding a kind is a schema change; the mapper's kind dispatch and
 * the entity's semantics must be updated together.
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
    ;

    companion object {
        fun fromId(id: String): PlantMultiValueKind? = entries.firstOrNull { it.id == id }
    }
}
