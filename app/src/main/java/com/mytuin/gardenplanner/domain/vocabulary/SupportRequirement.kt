package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SupportRequirement.
 *
 * PLANT_VOCABULARIES.md §23. Five values.
 *
 * The type of support is separate from this vocabulary. Specific
 * support relationships belong to garden/planning data.
 */
enum class SupportRequirement(
    override val id: String,
) : VocabularyValue {
    NONE("none"),
    OPTIONAL("optional"),
    RECOMMENDED("recommended"),
    REQUIRED("required"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SupportRequirement? = entries.firstOrNull { it.id == id }
    }
}
