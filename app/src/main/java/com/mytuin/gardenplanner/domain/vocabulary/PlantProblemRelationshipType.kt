package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantProblemRelationshipType.
 *
 * V1_DATABASE_SCHEMA §23. Three values from §23's examples.
 *
 * This describes the nature of the link between a plant and a
 * problem. Where a finer response characteristic is known, it is
 * stored separately in ProblemResponse (P41D-8).
 */
enum class PlantProblemRelationshipType(
    override val id: String,
) : VocabularyValue {
    ASSOCIATED_WITH("associated_with"),
    SUSCEPTIBLE_TO("susceptible_to"),
    RESISTANT_TO("resistant_to"),
    ;

    companion object {
        fun fromId(id: String): PlantProblemRelationshipType? = entries.firstOrNull { it.id == id }
    }
}
