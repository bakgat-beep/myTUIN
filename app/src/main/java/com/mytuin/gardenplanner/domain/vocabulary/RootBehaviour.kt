package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RootBehaviour.
 *
 * PLANT_VOCABULARIES.md §22. Five values.
 *
 * Multiple values may apply. Specific root depth and spread remain
 * structured data, added in a later sub-step (4c).
 */
enum class RootBehaviour(
    override val id: String,
) : VocabularyValue {
    SHALLOW("shallow"),
    MODERATE("moderate"),
    DEEP("deep"),
    SPREADING("spreading"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): RootBehaviour? = entries.firstOrNull { it.id == id }
    }
}
