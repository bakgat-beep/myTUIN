package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PollinationRequirement.
 *
 * PLANT_VOCABULARIES.md §24. Four values.
 *
 * A plant that benefits from a partner despite being self-fertile
 * should not receive a separate requirement value solely for that
 * reason. Specific compatible pollination partners are plant
 * relationships/knowledge.
 */
enum class PollinationRequirement(
    override val id: String,
) : VocabularyValue {
    SELF_FERTILE("self_fertile"),
    PARTIALLY_SELF_FERTILE("partially_self_fertile"),
    CROSS_POLLINATION_REQUIRED("cross_pollination_required"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PollinationRequirement? = entries.firstOrNull { it.id == id }
    }
}
