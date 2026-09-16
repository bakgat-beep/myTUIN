package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GrowingSpaceType.
 *
 * GARDEN_VOCABULARIES.md §4. All 16 values included per A52; the
 * vocabulary is canonical and subsetting would require a documented
 * reason (DEC-040).
 *
 * Canonical ids only (DEC-040). Domain layer: no Android, Compose,
 * Room or Hilt dependencies.
 */
enum class GrowingSpaceType(override val id: String) : VocabularyValue {
    GARDEN_BED("garden_bed"),
    RAISED_BED("raised_bed"),
    IN_GROUND_BED("in_ground_bed"),
    CONTAINER("container"),
    POT("pot"),
    PLANTER("planter"),
    GREENHOUSE("greenhouse"),
    POLYTUNNEL("polytunnel"),
    ORCHARD("orchard"),
    ROW("row"),
    BORDER("border"),
    VERTICAL_SPACE("vertical_space"),
    NURSERY_AREA("nursery_area"),
    LAWN("lawn"),
    MIXED_AREA("mixed_area"),
    OTHER("other"),
    ;

    companion object {
        fun fromId(id: String): GrowingSpaceType? =
            entries.firstOrNull { it.id == id }
    }
}