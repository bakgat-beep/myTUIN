package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ToxicPart.
 *
 * PLANT_VOCABULARIES §41 `toxic_parts`. Multi-value; a plant may
 * have several toxic parts.
 */
enum class ToxicPart(
    override val id: String,
) : VocabularyValue {
    LEAVES("leaves"),
    STEMS("stems"),
    ROOTS("roots"),
    FRUIT("fruit"),
    SEEDS("seeds"),
    FLOWERS("flowers"),
    SAP("sap"),
    BARK("bark"),
    WHOLE_PLANT("whole_plant"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): ToxicPart? = entries.firstOrNull { it.id == id }
    }
}
