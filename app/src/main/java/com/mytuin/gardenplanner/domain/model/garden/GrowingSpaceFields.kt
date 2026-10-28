package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType

/**
 * The mutable scalar fields of a GrowingSpace (Step 9d, D6 (a)).
 *
 * Bundles the fields that `UpdateGrowingSpaceFields` is allowed to
 * change. Geometry and status are deliberately absent: geometry
 * changes go through `UpdateGrowingSpaceGeometry` so that history
 * is preserved (DEC-041); status changes go through archive/restore.
 *
 * All six dimensions are nullable and represent "not set" when null.
 */
data class GrowingSpaceFields(
    val name: String,
    val spaceType: GrowingSpaceType,
    val description: String?,
    val lengthMetres: Double?,
    val widthMetres: Double?,
    val heightMetres: Double?,
    val diameterMetres: Double?,
    val areaSquareMetres: Double?,
    val volumeCubicMetres: Double?,
    val notes: String?,
)
