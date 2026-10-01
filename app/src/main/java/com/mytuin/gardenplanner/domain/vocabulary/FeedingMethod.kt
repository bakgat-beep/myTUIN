package com.mytuin.gardenplanner.domain.vocabulary

/**
 * FeedingMethod.
 *
 * ACTIVITY_VOCABULARIES.md §7. Values exactly as listed.
 */
enum class FeedingMethod(
    override val id: String,
) : VocabularyValue {
    SURFACE_APPLICATION("surface_application"),
    INCORPORATED("incorporated"),
    LIQUID_FEED("liquid_feed"),
    FOLIAR_FEED("foliar_feed"),
    COMPOST_APPLICATION("compost_application"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): FeedingMethod? = entries.firstOrNull { it.id == id }
    }
}
