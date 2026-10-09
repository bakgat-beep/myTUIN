package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GeographicScope.
 *
 * CORE_VOCABULARIES.md §10. All 8 values included.
 *
 * Describes the intended applicability of information. The actual
 * location is represented through structured geographic data
 * elsewhere; this is a classification of scope, not a place.
 */
enum class GeographicScope(
    override val id: String,
) : VocabularyValue {
    GLOBAL("global"),
    HEMISPHERE("hemisphere"),
    COUNTRY("country"),
    REGION("region"),
    CLIMATE_ZONE("climate_zone"),
    LOCALITY("locality"),
    GARDEN("garden"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): GeographicScope? = entries.firstOrNull { it.id == id }
    }
}
