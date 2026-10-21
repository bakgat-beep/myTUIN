package com.mytuin.gardenplanner.domain.vocabulary

/**
 * InboxItemRelatedEntityType.
 *
 * E6. The kind of entity an InboxItem relates to. Derived from the
 * "may be generated from" list in V1_DATABASE_SCHEMA §52: Plans,
 * Recommendations, Problems, observations, experiments.
 *
 * Polymorphic `related_entity_id` has no FK.
 */
enum class InboxItemRelatedEntityType(
    override val id: String,
) : VocabularyValue {
    PLAN("plan"),
    RECOMMENDATION("recommendation"),
    PROBLEM("problem"),
    OBSERVATION("observation"),
    EXPERIMENT("experiment"),
    ;

    companion object {
        fun fromId(id: String): InboxItemRelatedEntityType? = entries.firstOrNull { it.id == id }
    }
}
