package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantLifecycle.
 *
 * PLANT_VOCABULARIES.md §3. Canonical ids only; the enum constant
 * name is a code-level convenience and is never stored (DEC-040).
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
enum class PlantLifecycle(val id: String) {
    ANNUAL("annual"),
    BIENNIAL("biennial"),
    PERENNIAL("perennial"),
    SHORT_LIVED_PERENNIAL("short_lived_perennial"),
    WOODY_PERENNIAL("woody_perennial"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PlantLifecycle? =
            entries.firstOrNull { it.id == id }
    }
}