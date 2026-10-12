package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantNoteKind.
 *
 * Discriminator for the plant_note table (step 4c-B). Each kind
 * names one of the §41 free-text guidance fields.
 *
 * `rotation_notes` is deliberately absent: it is deferred to step 4e
 * alongside the rotation_group reference entity (P41C-9).
 */
enum class PlantNoteKind(
    override val id: String,
) : VocabularyValue {
    TRANSPLANTING_NOTES("transplanting_notes"),
    FEEDING("feeding"),
    WATERING("watering"),
    PRUNING("pruning"),
    OTHER_MAINTENANCE("other_maintenance"),
    HARVEST_INDICATORS("harvest_indicators"),
    HARVEST_METHOD("harvest_method"),
    STORAGE("storage"),
    CULINARY_USES("culinary_uses"),
    POLLINATION_NOTES("pollination_notes"),
    REGIONAL_NOTES("regional_notes"),
    CLIMATE_NOTES("climate_notes"),
    SAFETY_NOTES("safety_notes"),
    ;

    companion object {
        fun fromId(id: String): PlantNoteKind? = entries.firstOrNull { it.id == id }
    }
}
