package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Confidence.
 *
 * CORE_VOCABULARIES.md §5. All 6 values.
 *
 * Confidence is contextual: it attaches to a specific claim or
 * assessment, not to a record in general. A high-confidence
 * observation does not automatically produce a high-confidence
 * inference (CORE_VOCABULARIES §5).
 *
 * not_assessed is a distinct value from the four ratings. An
 * observation whose confidence has not been assessed is represented
 * by not_assessed, not by a default moderate.
 */
enum class Confidence(
    override val id: String,
) : VocabularyValue {
    VERY_LOW("very_low"),
    LOW("low"),
    MODERATE("moderate"),
    HIGH("high"),
    VERY_HIGH("very_high"),
    NOT_ASSESSED("not_assessed"),
    ;

    companion object {
        fun fromId(id: String): Confidence? = entries.firstOrNull { it.id == id }
    }
}
