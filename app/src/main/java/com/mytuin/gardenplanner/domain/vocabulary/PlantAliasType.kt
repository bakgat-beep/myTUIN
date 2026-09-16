package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantAliasType.
 *
 * Values derived from the example list in V1_DATABASE_SCHEMA.md §17.
 *
 * Canonical ids only (DEC-040). Domain layer: no Android, Compose,
 * Room or Hilt dependencies.
 */
enum class PlantAliasType(override val id: String) : VocabularyValue {
    COMMON_NAME("common_name"),
    REGIONAL_NAME("regional_name"),
    SYNONYM("synonym"),
    PLURAL("plural"),
    SEARCH_TERM("search_term"),
    ;

    companion object {
        fun fromId(id: String): PlantAliasType? =
            entries.firstOrNull { it.id == id }
    }
}