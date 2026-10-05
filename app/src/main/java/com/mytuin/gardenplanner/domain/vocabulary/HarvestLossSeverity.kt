package com.mytuin.gardenplanner.domain.vocabulary

/**
 * HarvestLossSeverity.
 *
 * HL9. Shares the value set with PROBLEM_VOCABULARIES §15 (problem
 * severity) but is a distinct vocabulary for a distinct context, per
 * VOCABULARY_INDEX §13.2. Both scales are used to describe "how bad"
 * something is, but a loss severity and a problem severity are not
 * interchangeable classifications.
 *
 * `unknown` is a value; null means "not recorded".
 */
enum class HarvestLossSeverity(
    override val id: String,
) : VocabularyValue {
    MINOR("minor"),
    MODERATE("moderate"),
    MAJOR("major"),
    CRITICAL("critical"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): HarvestLossSeverity? = entries.firstOrNull { it.id == id }
    }
}
