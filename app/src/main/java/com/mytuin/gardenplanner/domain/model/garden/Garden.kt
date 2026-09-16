package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Garden — domain model.
 *
 * V1_DATABASE_SCHEMA.md §11. DATA_MODEL.md §5.
 * Field names are camelCase — see Plant.kt for the mapping rationale.
 *
 * Location is structured data, not vocabulary (V1_DATABASE_SCHEMA §11;
 * CORE_ARCHITECTURE §22). Exact coordinates are private (V1_DATABASE_SCHEMA
 * §11; CORE_ARCHITECTURE §80).
 */
data class Garden(
    val id: String,
    val name: String,
    val description: String?,
    val countryCode: String?,
    val region: String?,
    val locality: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timezone: String?,
    val hemisphere: Hemisphere,
    val status: RecordStatus,
    val createdAt: Long,
    val updatedAt: Long,
)