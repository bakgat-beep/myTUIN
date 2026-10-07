package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ProblemStatus.
 *
 * PROBLEM_VOCABULARIES.md §16. Seven values.
 *
 * Distinct from RecordStatus. A Problem may be `active` (its
 * ProblemStatus) while the record itself is `archived` (its
 * RecordStatus). The two are orthogonal (P2 (a)).
 *
 * §16 notes: "confirmed should indicate that there is sufficient
 * evidence or explicit user confirmation for the application's
 * intended use. The application should not imply scientific
 * certainty merely because a user selected confirmed."
 */
enum class ProblemStatus(
    override val id: String,
) : VocabularyValue {
    SUSPECTED("suspected"),
    CONFIRMED("confirmed"),
    ACTIVE("active"),
    IMPROVING("improving"),
    RESOLVED("resolved"),
    RECURRING("recurring"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): ProblemStatus? = entries.firstOrNull { it.id == id }
    }
}
