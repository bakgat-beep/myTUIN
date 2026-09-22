package com.mytuin.gardenplanner.domain.vocabulary

/**
 * InfrastructureType.
 *
 * GARDEN_VOCABULARIES.md §5. All 18 values included; the vocabulary
 * is canonical and subsetting would require a documented reason
 * (DEC-040).
 *
 * B1: named InfrastructureType, matching the vocabulary document
 * that owns the values. The database column is object_type, matching
 * V1_DATABASE_SCHEMA §14 which owns the entity shape. This
 * deliberate divergence avoids colliding with GARDEN_VOCABULARIES §6
 * which defines a differently-shaped vocabulary called "Spatial
 * object type".
 *
 * B3: tree and root_zone are not in this vocabulary by design. They
 * are plant-side concepts, not garden infrastructure.
 */
enum class InfrastructureType(
    override val id: String,
) : VocabularyValue {
    PATH("path"),
    FENCE("fence"),
    WALL("wall"),
    GATE("gate"),
    BUILDING("building"),
    SHED("shed"),
    GREENHOUSE_STRUCTURE("greenhouse_structure"),
    IRRIGATION("irrigation"),
    WATER_SOURCE("water_source"),
    DRAIN("drain"),
    COMPOST_AREA("compost_area"),
    STORAGE_AREA("storage_area"),
    SUPPORT_STRUCTURE("support_structure"),
    TRELLIS("trellis"),
    SHADE_STRUCTURE("shade_structure"),
    LIGHTING("lighting"),
    UTILITY("utility"),
    OTHER("other"),
    ;

    companion object {
        fun fromId(id: String): InfrastructureType? = entries.firstOrNull { it.id == id }
    }
}
