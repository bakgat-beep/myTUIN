package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Cultivar — domain model.
 *
 * V1_DATABASE_SCHEMA.md §18. DATA_MODEL.md §14.
 *
 * A Cultivar belongs to exactly one Plant. Cultivar-specific knowledge
 * (maturity, frost sensitivity, etc.) is not modelled here — see the
 * entity-level comment in CultivarEntity for the deferral rationale.
 */
data class Cultivar(
    val id: String,
    val plantId: String,
    val name: String,
    val description: String?,
    val notes: String?,
    val status: RecordStatus,
    val createdAt: Long,
    val updatedAt: Long,
)