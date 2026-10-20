package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SuitabilityDimension.
 *
 * PLANNING_VOCABULARIES.md §7. Fifteen values.
 *
 * A recommendation does not need to evaluate every dimension. Only
 * materially relevant dimensions should normally be stored.
 */
enum class SuitabilityDimension(
    override val id: String,
) : VocabularyValue {
    LIGHT("light"),
    WATER("water"),
    MOISTURE("moisture"),
    SOIL("soil"),
    PH("ph"),
    FERTILITY("fertility"),
    NUTRIENTS("nutrients"),
    TEMPERATURE("temperature"),
    FROST("frost"),
    TIMING("timing"),
    SPACE("space"),
    ROTATION("rotation"),
    RELATIONSHIPS("relationships"),
    INFRASTRUCTURE("infrastructure"),
    HISTORY("history"),
    ;

    companion object {
        fun fromId(id: String): SuitabilityDimension? = entries.firstOrNull { it.id == id }
    }
}
