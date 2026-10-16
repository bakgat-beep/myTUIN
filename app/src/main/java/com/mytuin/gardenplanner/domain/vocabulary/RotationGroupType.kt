package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RotationGroupType.
 *
 * PLANNING_VOCABULARIES.md §17. Two values.
 *
 * System-defined rotation groups represent established application
 * knowledge and exist application-wide (garden_id null). User-defined
 * rotation groups belong to a specific garden (garden_id non-null).
 *
 * VOCABULARY_INDEX §3.3: a user-created classification must remain
 * associated with its creator/garden context. It must not
 * automatically become global system knowledge.
 */
enum class RotationGroupType(
    override val id: String,
) : VocabularyValue {
    SYSTEM("system"),
    USER_DEFINED("user_defined"),
    ;

    companion object {
        fun fromId(id: String): RotationGroupType? = entries.firstOrNull { it.id == id }
    }
}
