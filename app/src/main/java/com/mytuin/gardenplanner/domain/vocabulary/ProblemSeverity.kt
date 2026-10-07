package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ProblemSeverity.
 *
 * PROBLEM_VOCABULARIES.md §15. Five values.
 *
 * §15 is explicit: "Severity is not the same as confidence." A major
 * but uncertain problem (severity = major, confidence = low) is a
 * legitimate state.
 */
enum class ProblemSeverity(
    override val id: String,
) : VocabularyValue {
    MINOR("minor"),
    MODERATE("moderate"),
    MAJOR("major"),
    CRITICAL("critical"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): ProblemSeverity? = entries.firstOrNull { it.id == id }
    }
}
