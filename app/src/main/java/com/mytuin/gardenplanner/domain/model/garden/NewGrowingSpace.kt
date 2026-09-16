package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType

/**
 * User-settable fields for a new GrowingSpace (A62).
 *
 * The use case assigns id, status, createdAt and updatedAt. None of
 * these appear here so the caller cannot supply them.
 *
 * All dimensions in metres (A51=A).
 */
data class NewGrowingSpace(
    val gardenId: String,
    val name: String,
    val spaceType: GrowingSpaceType,
    val geometry: Geometry? = null,
    val lengthMetres: Double? = null,
    val widthMetres: Double? = null,
    val heightMetres: Double? = null,
    val diameterMetres: Double? = null,
    val areaSquareMetres: Double? = null,
    val volumeCubicMetres: Double? = null,
    val description: String? = null,
    val notes: String? = null,
)