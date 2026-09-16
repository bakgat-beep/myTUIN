package com.mytuin.gardenplanner.domain.model.garden

/**
 * A coordinate in the garden's local spatial frame.
 *
 * A50b=(i): x and y are metres relative to the garden's datum point
 * (by convention the garden's reference corner at (0, 0)). Not
 * latitude/longitude. Not degrees. No projection.
 *
 * The largest dimension of a typical domestic garden is under 100 m,
 * so Earth-curvature corrections within a garden are sub-millimetre
 * and are deliberately not modelled here.
 *
 * Latitude and longitude remain on the Garden for seasonal and
 * hemisphere purposes (V1_DATABASE_SCHEMA §11). They are not used
 * for geometry.
 */
data class Coordinate(
    val x: Double,
    val y: Double,
)