package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RecommendationStatus.
 *
 * PLANNING_VOCABULARIES.md §5. Eight values.
 *
 * The application's overall assessment of an option. Must not be
 * interpreted as a numerical score (§5).
 *
 * `expired` is the retirement state (R7). No separate RecordStatus
 * is carried on Recommendation.
 */
enum class RecommendationStatus(
    override val id: String,
) : VocabularyValue {
    RECOMMENDED("recommended"),
    CONDITIONAL("conditional"),
    POSSIBLE("possible"),
    MARGINAL("marginal"),
    NOT_RECOMMENDED("not_recommended"),
    UNSUITABLE("unsuitable"),
    INSUFFICIENT_INFORMATION("insufficient_information"),
    EXPIRED("expired"),
    ;

    companion object {
        fun fromId(id: String): RecommendationStatus? = entries.firstOrNull { it.id == id }
    }
}
