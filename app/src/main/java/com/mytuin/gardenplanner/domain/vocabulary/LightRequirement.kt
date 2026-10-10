package com.mytuin.gardenplanner.domain.vocabulary

/**
 * LightRequirement.
 *
 * PLANT_VOCABULARIES.md §17. Five values.
 *
 * Describes preferred light, not tolerance. ShadeTolerance is a
 * separate classification (§18). Actual garden light conditions
 * belong to environmental/garden data.
 */
enum class LightRequirement(
    override val id: String,
) : VocabularyValue {
    FULL_SUN("full_sun"),
    PARTIAL_SHADE("partial_shade"),
    FULL_SHADE("full_shade"),
    VARIABLE("variable"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): LightRequirement? = entries.firstOrNull { it.id == id }
    }
}
