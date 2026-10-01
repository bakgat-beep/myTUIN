package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ActivityType.
 *
 * ACTIVITY_VOCABULARIES.md §3.1. All 11 values included; the
 * vocabulary is canonical and subsetting would require a documented
 * reason (DEC-040).
 *
 * S1 (ii): `observation` is a valid type. It represents the act of
 * recording an observation on the timeline. The observation's
 * content lives on the separate Observation entity (a later step),
 * not on this Activity.
 */
enum class ActivityType(
    override val id: String,
) : VocabularyValue {
    PLANTING("planting"),
    WATERING("watering"),
    FEEDING("feeding"),
    PRUNING("pruning"),
    HARVESTING("harvesting"),
    OBSERVATION("observation"),
    INTERVENTION("intervention"),
    SOIL_WORK("soil_work"),
    MAINTENANCE("maintenance"),
    MOVING("moving"),
    REMOVAL("removal"),
    ;

    companion object {
        fun fromId(id: String): ActivityType? = entries.firstOrNull { it.id == id }
    }
}
