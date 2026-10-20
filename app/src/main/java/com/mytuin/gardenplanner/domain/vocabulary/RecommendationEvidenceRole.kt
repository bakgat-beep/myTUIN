package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RecommendationEvidenceRole.
 *
 * R5. How a piece of evidence relates to a recommendation. Value set
 * mirrors ProblemEvidenceDirection (Step 3e) but is a distinct
 * vocabulary, per VOCABULARY_INDEX §13.2: similar words in different
 * contexts remain separate classifications.
 */
enum class RecommendationEvidenceRole(
    override val id: String,
) : VocabularyValue {
    SUPPORTS("supports"),
    WEAKLY_SUPPORTS("weakly_supports"),
    NEUTRAL("neutral"),
    WEAKLY_CONTRADICTS("weakly_contradicts"),
    CONTRADICTS("contradicts"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): RecommendationEvidenceRole? = entries.firstOrNull { it.id == id }
    }
}
