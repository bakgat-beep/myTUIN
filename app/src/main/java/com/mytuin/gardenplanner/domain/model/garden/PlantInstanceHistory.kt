package com.mytuin.gardenplanner.domain.model.garden

/**
 * PlantInstanceHistory — domain model for a past location of a
 * PlantInstance.
 *
 * DEC-041. Append-only; each row captures the prior location and the
 * period it was effective: [validFrom, validTo).
 *
 * Location is composite: growingSpaceId, spatialObjectId, geometry.
 * Any of the three may be null. A history row captures the state of
 * all three at the moment they were superseded.
 *
 * History rows are never updated in place.
 */
data class PlantInstanceHistory(
    val id: String,
    val plantInstanceId: String,
    val growingSpaceId: String?,
    val spatialObjectId: String?,
    val geometry: Geometry?,
    val validFrom: Long,
    val validTo: Long,
    val recordedAt: Long,
    val reason: String?,
)
