package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Plant — domain model.
 *
 * V1_DATABASE_SCHEMA.md §16. DATA_MODEL.md §12.
 * Plant is general horticultural knowledge, not a user's actual planting
 * (CORE_ARCHITECTURE §10, §11).
 *
 * Field names are camelCase — idiomatic Kotlin for the domain layer.
 * The Room entity uses snake_case to match the schema JSON. The mapper
 * in the data layer converts between them (A28; V1_TECHNICAL_ARCHITECTURE
 * §79).
 *
 * lifecycle is nullable. PLANT_VOCABULARIES §3 provides an UNKNOWN
 * value, but "no lifecycle recorded" and "lifecycle recorded as
 * unknown" remain distinguishable (CORE_VOCABULARIES §4).
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
data class Plant(
    val id: String,
    val canonicalName: String,
    val scientificName: String?,
    val genus: String?,
    val species: String?,
    val family: String?,
    val lifecycle: PlantLifecycle?,
    val description: String?,
    val status: RecordStatus,
    val createdAt: Long,
    val updatedAt: Long,
)
