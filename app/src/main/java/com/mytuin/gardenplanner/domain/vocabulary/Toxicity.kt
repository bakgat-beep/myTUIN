package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Toxicity.
 *
 * PLANT_VOCABULARIES §41 `toxicity`. No dedicated section. A broad
 * safety classification, not a detailed toxicology record.
 *
 * Toxic parts are recorded separately as PlantMultiValue.ToxicPart
 * entries (§41 `toxic_parts`).
 */
enum class Toxicity(
    override val id: String,
) : VocabularyValue {
    NON_TOXIC("non_toxic"),
    MILDLY_TOXIC("mildly_toxic"),
    TOXIC("toxic"),
    HIGHLY_TOXIC("highly_toxic"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): Toxicity? = entries.firstOrNull { it.id == id }
    }
}
