package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantRelationshipType.
 *
 * V1_DATABASE_SCHEMA §22. Combines the three §41 fields
 * (`companion_plants`, `plants_to_avoid`, `pollination_partners`)
 * with the other §22 examples into one flat classification
 * (P41D-1 (a)).
 */
enum class PlantRelationshipType(
    override val id: String,
) : VocabularyValue {
    COMPANION("companion"),
    COMPETITIVE("competitive"),
    POLLINATION_PARTNER("pollination_partner"),
    ROTATION_RELATED("rotation_related"),
    SUCCESSION("succession"),
    SUPPORTS("supports"),
    AVOID("avoid"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PlantRelationshipType? = entries.firstOrNull { it.id == id }
    }
}
