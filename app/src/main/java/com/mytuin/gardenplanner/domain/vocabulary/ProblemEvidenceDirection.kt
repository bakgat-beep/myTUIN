package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ProblemEvidenceDirection.
 *
 * PROBLEM_VOCABULARIES.md §22. Six values.
 *
 * Describes how a piece of evidence relates to a hypothesis. It is
 * not a confidence level. Example: an observation of white powder on
 * leaves "supports" a powdery-mildew hypothesis; the strength of that
 * support is the hypothesis's confidence, which is a separate value.
 */
enum class ProblemEvidenceDirection(
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
        fun fromId(id: String): ProblemEvidenceDirection? = entries.firstOrNull { it.id == id }
    }
}
