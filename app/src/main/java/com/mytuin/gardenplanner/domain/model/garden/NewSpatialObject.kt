package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * User-settable fields for a new SpatialObject.
 *
 * The use case assigns id, status, createdAt and updatedAt.
 * geometry is required (A5).
 */
data class NewSpatialObject(
    val gardenId: String,
    val objectType: InfrastructureType,
    val geometry: Geometry,
    val name: String? = null,
    val description: String? = null,
    val areaId: String? = null,
    val notes: String? = null,
)
