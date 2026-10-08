package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlanningStatus.
 *
 * PLANNING_VOCABULARIES.md §4. Ten values.
 *
 * Distinct from RecordStatus (PL5). A Plan may be `completed` in its
 * planning lifecycle while the record is `archived`, or `idea` while
 * the record is `active`.
 *
 * §4 note: "A planning status describes the plan. It does not
 * constitute evidence that a real-world activity occurred." Actual
 * activities must be recorded through the Activity model.
 */
enum class PlanningStatus(
    override val id: String,
) : VocabularyValue {
    IDEA("idea"),
    PLANNED("planned"),
    SCHEDULED("scheduled"),
    IN_PROGRESS("in_progress"),
    PARTIALLY_COMPLETED("partially_completed"),
    COMPLETED("completed"),
    SKIPPED("skipped"),
    CANCELLED("cancelled"),
    EXPIRED("expired"),
    SUPERSEDED("superseded"),
    ;

    companion object {
        fun fromId(id: String): PlanningStatus? = entries.firstOrNull { it.id == id }
    }
}
