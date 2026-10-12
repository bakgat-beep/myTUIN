package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PropagationMethod.
 *
 * PLANT_VOCABULARIES.md §10. All 10 values included.
 *
 * How a plant can be propagated. Multiple values may apply. Detailed
 * propagation instructions belong to plant knowledge.
 */
enum class PropagationMethod(
    override val id: String,
) : VocabularyValue {
    SEED("seed"),
    DIVISION("division"),
    CUTTING("cutting"),
    LAYERING("layering"),
    GRAFTING("grafting"),
    BUDDING("budding"),
    OFFSET("offset"),
    RUNNER("runner"),
    TISSUE_CULTURE("tissue_culture"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PropagationMethod? = entries.firstOrNull { it.id == id }
    }
}
