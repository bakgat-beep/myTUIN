package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ExperimentStatus.
 *
 * PLANNING_VOCABULARIES.md §18. Six values.
 *
 * Describes the lifecycle of a garden experiment. Observed results
 * are represented through ordinary garden records and experiment
 * relationships, not through this status.
 */
enum class ExperimentStatus(
    override val id: String,
) : VocabularyValue {
    DRAFT("draft"),
    PLANNED("planned"),
    ACTIVE("active"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    ABANDONED("abandoned"),
    ;

    companion object {
        fun fromId(id: String): ExperimentStatus? = entries.firstOrNull { it.id == id }
    }
}
