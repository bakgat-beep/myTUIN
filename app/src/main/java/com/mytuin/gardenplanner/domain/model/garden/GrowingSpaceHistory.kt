package com.mytuin.gardenplanner.domain.model.garden

/**
 * GrowingSpaceHistory — domain model for a past state of a GrowingSpace.
 *
 * DEC-041: state records preserve history via a companion append-only
 * table. Each row captures the prior state (geometry and dimensions)
 * and the period it was effective: [validFrom, validTo).
 *
 * History rows are never updated in place.
 *
 * Dimensions in metres (A51=A). area/volume in square/cubic metres.
 */
data class GrowingSpaceHistory(
    val id: String,
    val growingSpaceId: String,
    val geometry: Geometry?,
    val lengthMetres: Double?,
    val widthMetres: Double?,
    val heightMetres: Double?,
    val diameterMetres: Double?,
    val areaSquareMetres: Double?,
    val volumeCubicMetres: Double?,
    val validFrom: Long,
    val validTo: Long,
    val recordedAt: Long,
    val reason: String?,
)
