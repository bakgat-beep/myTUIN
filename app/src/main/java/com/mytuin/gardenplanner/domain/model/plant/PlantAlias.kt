package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType

/**
 * PlantAlias — domain model.
 *
 * V1_DATABASE_SCHEMA.md §17. DATA_MODEL.md §13.
 *
 * language is optional ISO 639-1 (A10). Language is data, not
 * vocabulary.
 */
data class PlantAlias(
    val id: String,
    val plantId: String,
    val alias: String,
    val aliasType: PlantAliasType,
    val language: String?,
)
