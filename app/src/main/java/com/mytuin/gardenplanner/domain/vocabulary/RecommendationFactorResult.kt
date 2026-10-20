package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RecommendationFactorResult.
 *
 * PLANNING_VOCABULARIES.md §6. Five values.
 *
 * A factor result is not a confidence. `unknown` means the factor
 * cannot be assessed with the available information.
 */
enum class RecommendationFactorResult(
    override val id: String,
) : VocabularyValue {
    EXCELLENT("excellent"),
    GOOD("good"),
    MARGINAL("marginal"),
    POOR("poor"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): RecommendationFactorResult? = entries.firstOrNull { it.id == id }
    }
}
