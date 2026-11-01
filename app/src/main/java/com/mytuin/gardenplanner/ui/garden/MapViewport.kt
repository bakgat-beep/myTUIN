package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry

/**
 * Bounding box in the garden's local metre frame (Step 9f).
 *
 * Used to fit the MapLibre camera so all shapes are visible on
 * first composition and when the user taps re-centre. The fallback
 * bounds give the camera somewhere sensible to point when a garden
 * exists but has no geometry yet.
 */
data class MapViewport(
    val minX: Double,
    val minY: Double,
    val maxX: Double,
    val maxY: Double,
) {
    val centerX: Double get() = (minX + maxX) / 2.0
    val centerY: Double get() = (minY + maxY) / 2.0
    val widthMetres: Double get() = maxX - minX
    val heightMetres: Double get() = maxY - minY
    val largestDimensionMetres: Double get() = maxOf(widthMetres, heightMetres)

    companion object {
        private const val PADDING_METRES = 3.0
        private const val FALLBACK_HALF_EXTENT = 15.0

        val FALLBACK: MapViewport =
            MapViewport(
                minX = -FALLBACK_HALF_EXTENT,
                minY = -FALLBACK_HALF_EXTENT,
                maxX = FALLBACK_HALF_EXTENT,
                maxY = FALLBACK_HALF_EXTENT,
            )

        /**
         * Union of all coordinates across every shape, expanded by
         * PADDING_METRES on each side so shapes are not flush with
         * the screen edge. Returns FALLBACK when shapes contain no
         * coordinates.
         */
        fun union(shapes: List<MapShape>): MapViewport {
            val coords = shapes.flatMap { shape -> shape.geometry.allCoordinates() }
            if (coords.isEmpty()) return FALLBACK
            val xs = coords.map { it.x }
            val ys = coords.map { it.y }
            return MapViewport(
                minX = xs.min() - PADDING_METRES,
                minY = ys.min() - PADDING_METRES,
                maxX = xs.max() + PADDING_METRES,
                maxY = ys.max() + PADDING_METRES,
            )
        }

        private fun Geometry.allCoordinates(): List<com.mytuin.gardenplanner.domain.model.garden.Coordinate> =
            when (this) {
                is Geometry.Point -> listOf(at)
                is Geometry.LineString -> vertices
                is Geometry.Polygon -> ring
            }
    }
}
