package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * SpatialObject — domain model.
 *
 * V1_DATABASE_SCHEMA.md §14. DATA_MODEL.md §9.
 *
 * A spatial object is a physical or conceptual garden feature that is
 * not itself a GrowingSpace: a path, a fence, an irrigation line.
 * Objects may overlap and need not belong to a parent (V1_DATABASE_SCHEMA
 * §14).
 *
 * geometry is non-null (A5): an object with no location is not a
 * spatial object.
 *
 * name is nullable (B4): a feature's identity may be carried purely
 * by its objectType when the type is unique in the garden.
 *
 * status is non-null (B6, A6): V1_DATABASE_SCHEMA §14 lists it as
 * required, unlike Area where §12 lists it as optional.
 */
data class SpatialObject(
    val id: String,
    val gardenId: String,
    val objectType: InfrastructureType,
    val geometry: Geometry,
    val status: RecordStatus,
    val name: String?,
    val description: String?,
    val areaId: String?,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
