package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantingMethod.
 *
 * ACTIVITY_VOCABULARIES.md §5. Values exactly as listed.
 */
enum class PlantingMethod(
    override val id: String,
) : VocabularyValue {
    DIRECT_SOW("direct_sow"),
    TRANSPLANT("transplant"),
    PLANTING_BARE_ROOT("planting_bare_root"),
    PLANTING_CONTAINER("planting_container"),
    DIVISION("division"),
    PROPAGATION("propagation"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PlantingMethod? = entries.firstOrNull { it.id == id }
    }
}
