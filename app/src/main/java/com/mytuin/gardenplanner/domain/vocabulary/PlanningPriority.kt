package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlanningPriority.
 *
 * PLANNING_VOCABULARIES.md §16. Four values.
 *
 * PL4: kept separate from GardenPriority despite identical value
 * labels. Per VOCABULARY_INDEX §13.2, similar words in different
 * contexts must remain separate vocabularies. A garden-affecting
 * preference priority and a plan's priority are different concepts.
 */
enum class PlanningPriority(
    override val id: String,
) : VocabularyValue {
    LOW("low"),
    NORMAL("normal"),
    HIGH("high"),
    CRITICAL("critical"),
    ;

    companion object {
        fun fromId(id: String): PlanningPriority? = entries.firstOrNull { it.id == id }
    }
}
