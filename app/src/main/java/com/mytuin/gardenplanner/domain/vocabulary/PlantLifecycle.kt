package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantLifecycle.
 *
 * PLANT_VOCABULARIES.md §3 (v0.4, September 2026). Canonical ids
 * only; the enum constant name is a code-level convenience and is
 * never stored (DEC-040).
 *
 * v0.3 included short_lived_perennial and woody_perennial. v0.4
 * reduced the vocabulary to four values. The removal is safe because
 * no stored data references the removed ids: seed data uses
 * lifecycle = NULL, and Phase 0 never shipped to a production device.
 *
 * Lifespan beyond this broad classification remains plant knowledge
 * or structured data (§3).
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
enum class PlantLifecycle(
    override val id: String,
) : VocabularyValue {
    ANNUAL("annual"),
    BIENNIAL("biennial"),
    PERENNIAL("perennial"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PlantLifecycle? = entries.firstOrNull { it.id == id }
    }
}
