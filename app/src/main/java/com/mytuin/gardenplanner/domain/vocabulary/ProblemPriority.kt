package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ProblemPriority.
 *
 * PROBLEM_VOCABULARIES §17. Four values.
 *
 * Distinct from ProblemSeverity (§15): severity is how bad the
 * problem is; priority is how urgently it needs action. §17 gives
 * the example "severity = moderate, priority = high" when a
 * moderate problem requires prompt intervention because of timing.
 *
 * No `unknown` value; the field is nullable, so absence means "not
 * recorded".
 */
enum class ProblemPriority(
    override val id: String,
) : VocabularyValue {
    LOW("low"),
    MODERATE("moderate"),
    HIGH("high"),
    URGENT("urgent"),
    ;

    companion object {
        fun fromId(id: String): ProblemPriority? = entries.firstOrNull { it.id == id }
    }
}
