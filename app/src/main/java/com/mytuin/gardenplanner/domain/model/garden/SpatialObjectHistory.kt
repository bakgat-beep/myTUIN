package com.mytuin.gardenplanner.domain.model.garden

/**
 * SpatialObjectHistory — domain model for a past state of a
 * SpatialObject.
 *
 * DEC-041 / DEC-051: state records preserve history via a companion
 * append-only table. Each row captures the prior geometry and the
 * period it was effective: [validFrom, validTo).
 *
 * Unlike GrowingSpaceHistory, there are no dimension fields: the
 * SpatialObject current table carries none, so history mirrors only
 * what the current table stores.
 *
 * Geometry is non-null (A5): SpatialObject always has a geometry,
 * so history always carries one too.
 *
 * History rows are never updated in place.
 */
data class SpatialObjectHistory(
    val id: String,
    val spatialObjectId: String,
    val geometry: Geometry,
    val validFrom: Long,
    val validTo: Long,
    val recordedAt: Long,
    val reason: String?,
)
