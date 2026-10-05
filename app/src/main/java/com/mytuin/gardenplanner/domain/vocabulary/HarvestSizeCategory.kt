package com.mytuin.gardenplanner.domain.vocabulary

/**
 * HarvestSizeCategory.
 *
 * ACTIVITY_VOCABULARIES.md §21. Five values. No `unknown`: the field
 * is optional, and absence means "not assessed" (CORE_VOCABULARIES
 * §4). A user who has not classified size leaves it null.
 *
 * This is an observational classification, not a measurement. It
 * must not be interpreted as a precise size.
 */
enum class HarvestSizeCategory(
    override val id: String,
) : VocabularyValue {
    VERY_SMALL("very_small"),
    SMALL("small"),
    REGULAR("regular"),
    LARGE("large"),
    VERY_LARGE("very_large"),
    ;

    companion object {
        fun fromId(id: String): HarvestSizeCategory? = entries.firstOrNull { it.id == id }
    }
}
