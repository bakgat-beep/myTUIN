package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantRole.
 *
 * PLANT_VOCABULARIES.md §4. All 25 values included.
 *
 * A plant may have multiple roles. Multi-value through
 * PlantMultiValueKind.PLANT_ROLE (§28 note: `herb` is a
 * garden/culinary role here, not a botanical growth-form
 * classification).
 */
enum class PlantRole(
    override val id: String,
) : VocabularyValue {
    FOOD_CROP("food_crop"),
    FRUIT_CROP("fruit_crop"),
    HERB("herb"),
    LEAFY_GREEN("leafy_green"),
    ROOT_CROP("root_crop"),
    BULB_CROP("bulb_crop"),
    TUBER_CROP("tuber_crop"),
    LEGUME("legume"),
    GRAIN_CROP("grain_crop"),
    COVER_CROP("cover_crop"),
    GREEN_MANURE("green_manure"),
    ORNAMENTAL("ornamental"),
    POLLINATOR_SUPPORT("pollinator_support"),
    WILDLIFE_SUPPORT("wildlife_support"),
    MEDICINAL("medicinal"),
    AROMATIC("aromatic"),
    CULINARY("culinary"),
    NITROGEN_FIXER("nitrogen_fixer"),
    COMPANION("companion"),
    HEDGE("hedge"),
    SCREENING("screening"),
    WINDBREAK("windbreak"),
    GROUND_COVER("ground_cover"),
    SOIL_IMPROVEMENT("soil_improvement"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PlantRole? = entries.firstOrNull { it.id == id }
    }
}
