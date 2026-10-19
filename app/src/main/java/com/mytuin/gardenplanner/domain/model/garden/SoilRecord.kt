package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * SoilRecord — domain model.
 *
 * V1_DATABASE_SCHEMA §34. DATA_MODEL §30.
 *
 * The garden's recorded understanding of soil at a particular
 * spatial context. May apply to the garden (S5-1 (a): always), an
 * Area, or a GrowingSpace. At most one of areaId and growingSpaceId
 * is non-null (S5-1 (a), enforced in the mapper).
 *
 * Soil characteristics are not stored here. §34 is explicit that
 * they live in Observations and Measurements at the same spatial
 * scope. This record is a named container; it is not a source of
 * soil facts.
 *
 * S5-2 (a): no direct FK from Observation or Measurement to
 * SoilRecord. Evidence is correlated by spatial scope.
 *
 * S5-4 (a): no cached summary columns. §61 permits them; they are
 * not added in V1.
 */
data class SoilRecord(
    val id: String,
    val gardenId: String,
    val status: RecordStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val areaId: String?,
    val growingSpaceId: String?,
    val name: String?,
    val notes: String?,
)
