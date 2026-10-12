package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GrowingMethod.
 *
 * PLANT_VOCABULARIES.md §9. All 13 values included.
 *
 * Broad cultivation methods. Multiple values may apply. Specific
 * garden infrastructure belongs to garden/planning data.
 */
enum class GrowingMethod(
    override val id: String,
) : VocabularyValue {
    DIRECT_SOW("direct_sow"),
    TRANSPLANT("transplant"),
    CONTAINER("container"),
    RAISED_BED("raised_bed"),
    IN_GROUND("in_ground"),
    PROTECTED_CULTURE("protected_culture"),
    GREENHOUSE("greenhouse"),
    INDOORS("indoors"),
    OUTDOORS("outdoors"),
    HYDROPONIC("hydroponic"),
    AQUAPONIC("aquaponic"),
    VERTICAL("vertical"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): GrowingMethod? = entries.firstOrNull { it.id == id }
    }
}
