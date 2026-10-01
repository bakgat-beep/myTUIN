package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SoilWorkMethod.
 *
 * ACTIVITY_VOCABULARIES.md §9. Values exactly as listed.
 */
enum class SoilWorkMethod(
    override val id: String,
) : VocabularyValue {
    DIGGING("digging"),
    LOOSENING("loosening"),
    CULTIVATION("cultivation"),
    BED_PREPARATION("bed_preparation"),
    AMENDMENT_INCORPORATION("amendment_incorporation"),
    MULCHING("mulching"),
    DRAINAGE_WORK("drainage_work"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SoilWorkMethod? = entries.firstOrNull { it.id == id }
    }
}
