package com.mytuin.gardenplanner.domain.vocabulary

/**
 * UserDecisionType.
 *
 * CORE_VOCABULARIES.md §23. Six values.
 *
 * The gardener's choice. Does not prove that the resulting activity
 * occurred (CORE §23).
 */
enum class UserDecisionType(
    override val id: String,
) : VocabularyValue {
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    MODIFIED("modified"),
    OVERRIDDEN("overridden"),
    DEFERRED("deferred"),
    CANCELLED("cancelled"),
    ;

    companion object {
        fun fromId(id: String): UserDecisionType? = entries.firstOrNull { it.id == id }
    }
}
