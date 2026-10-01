package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ObservationType.
 *
 * ACTIVITY_VOCABULARIES.md §11. All 13 values included; the
 * vocabulary is canonical and subsetting would require a documented
 * reason (DEC-040).
 *
 * O4: V1_DATABASE_SCHEMA §30 lists a shorter, illustrative set. The
 * authoritative vocabulary is ACTIVITY_VOCABULARIES §11. `damage` is
 * not added; it is not in the vocabulary document.
 */
enum class ObservationType(
    override val id: String,
) : VocabularyValue {
    PLANT("plant"),
    SOIL("soil"),
    WATER("water"),
    WEATHER("weather"),
    PEST("pest"),
    DISEASE("disease"),
    GROWTH("growth"),
    FLOWERING("flowering"),
    FRUITING("fruiting"),
    HARVEST("harvest"),
    ENVIRONMENT("environment"),
    INFRASTRUCTURE("infrastructure"),
    GENERAL("general"),
    ;

    companion object {
        fun fromId(id: String): ObservationType? = entries.firstOrNull { it.id == id }
    }
}
