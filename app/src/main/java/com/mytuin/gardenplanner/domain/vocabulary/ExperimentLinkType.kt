package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ExperimentLinkType.
 *
 * E3. The kind of entity an Experiment is linked to.
 * V1_DATABASE_SCHEMA §49 lists Activities, Observations,
 * Measurements, Plans and Recommendations as linkable records.
 *
 * Polymorphic `linked_id` has no FK; validity is the caller's
 * responsibility.
 */
enum class ExperimentLinkType(
    override val id: String,
) : VocabularyValue {
    ACTIVITY("activity"),
    OBSERVATION("observation"),
    MEASUREMENT("measurement"),
    PLAN("plan"),
    RECOMMENDATION("recommendation"),
    ;

    companion object {
        fun fromId(id: String): ExperimentLinkType? = entries.firstOrNull { it.id == id }
    }
}
