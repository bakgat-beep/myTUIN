package com.mytuin.gardenplanner.domain.vocabulary

/**
 * FoodProductionCategory.
 *
 * PLANT_VOCABULARIES.md §5. All 14 values included.
 *
 * Describes the edible part harvested from the plant, not botanical
 * classification. Multiple values may apply. Replaces the
 * `harvestable_parts` field from §41, which describes the same
 * concept with the same values (P41B-1).
 */
enum class FoodProductionCategory(
    override val id: String,
) : VocabularyValue {
    FRUIT("fruit"),
    LEAF("leaf"),
    STEM("stem"),
    FLOWER("flower"),
    ROOT("root"),
    BULB("bulb"),
    TUBER("tuber"),
    SEED("seed"),
    GRAIN("grain"),
    POD("pod"),
    SHOOT("shoot"),
    WHOLE_PLANT("whole_plant"),
    MULTIPLE("multiple"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): FoodProductionCategory? = entries.firstOrNull { it.id == id }
    }
}
