package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PruningMethod.
 *
 * ACTIVITY_VOCABULARIES.md §8. Values exactly as listed.
 */
enum class PruningMethod(
    override val id: String,
) : VocabularyValue {
    DEADHEADING("deadheading"),
    THINNING("thinning"),
    HEADING_BACK("heading_back"),
    SHAPING("shaping"),
    REMOVAL_OF_DAMAGED_GROWTH("removal_of_damaged_growth"),
    REJUVENATION("rejuvenation"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PruningMethod? = entries.firstOrNull { it.id == id }
    }
}
