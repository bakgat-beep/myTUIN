package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SourceType.
 *
 * PROVENANCE_VOCABULARIES.md §6. All 10 values included.
 *
 * Classifies the kind of source. Distinct from evidence_type (§5),
 * which describes the nature of the evidence the source provides.
 */
enum class SourceType(
    override val id: String,
) : VocabularyValue {
    RESEARCH("research"),
    INSTITUTION("institution"),
    GOVERNMENT("government"),
    EXTENSION_SERVICE("extension_service"),
    EXPERT("expert"),
    COMMUNITY("community"),
    USER("user"),
    DATASET("dataset"),
    APPLICATION_GENERATED("application_generated"),
    OTHER("other"),
    ;

    companion object {
        fun fromId(id: String): SourceType? = entries.firstOrNull { it.id == id }
    }
}
