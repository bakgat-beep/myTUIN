package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SourceRelationship.
 *
 * PROVENANCE_VOCABULARIES §8. All 6 values included.
 *
 * Describes how a knowledge record relates to its source: directly
 * quoted, adapted, derived, summarised, inferred, or unknown.
 */
enum class SourceRelationship(
    override val id: String,
) : VocabularyValue {
    DIRECT("direct"),
    ADAPTED("adapted"),
    DERIVED("derived"),
    SUMMARISED("summarised"),
    INFERRED("inferred"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SourceRelationship? = entries.firstOrNull { it.id == id }
    }
}
