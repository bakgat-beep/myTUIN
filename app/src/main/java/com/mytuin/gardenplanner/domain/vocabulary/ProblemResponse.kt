package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ProblemResponse.
 *
 * PLANT_VOCABULARIES §27. All 7 values included.
 *
 * Describes how a plant or cultivar responds to a problem.
 * `resistant` does not mean immune (§27).
 */
enum class ProblemResponse(
    override val id: String,
) : VocabularyValue {
    HIGHLY_SUSCEPTIBLE("highly_susceptible"),
    SUSCEPTIBLE("susceptible"),
    MODERATELY_SUSCEPTIBLE("moderately_susceptible"),
    TOLERANT("tolerant"),
    RESISTANT("resistant"),
    HIGHLY_RESISTANT("highly_resistant"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): ProblemResponse? = entries.firstOrNull { it.id == id }
    }
}
