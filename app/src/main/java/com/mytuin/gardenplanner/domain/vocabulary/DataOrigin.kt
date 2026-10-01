package com.mytuin.gardenplanner.domain.vocabulary

/**
 * DataOrigin.
 *
 * PROVENANCE_VOCABULARIES.md §9. All 10 values included.
 *
 * Data origin describes how information entered the system. It is
 * distinct from evidence type and from confidence
 * (PROVENANCE_VOCABULARIES §9).
 */
enum class DataOrigin(
    override val id: String,
) : VocabularyValue {
    BUILT_IN("built_in"),
    USER_CREATED("user_created"),
    USER_OBSERVED("user_observed"),
    USER_MEASURED("user_measured"),
    USER_IMPORTED("user_imported"),
    EXTERNAL_DATASET("external_dataset"),
    COMMUNITY_CONTRIBUTED("community_contributed"),
    CURATED("curated"),
    SYSTEM_DERIVED("system_derived"),
    SYSTEM_INFERRED("system_inferred"),
    ;

    companion object {
        fun fromId(id: String): DataOrigin? = entries.firstOrNull { it.id == id }
    }
}
