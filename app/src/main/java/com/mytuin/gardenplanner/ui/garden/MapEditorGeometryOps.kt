package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry

private const val MINIMUM_POLYGON_RING_SIZE = 4

/**
 * Pure geometry operations used by the map editor (Step 9g).
 *
 * No MapLibre types. No Android. Testable in src/test.
 */
object MapEditorGeometryOps {
    fun translate(
        geometry: Geometry,
        delta: Coordinate,
    ): Geometry =
        when (geometry) {
            is Geometry.Point -> Geometry.Point(Coordinate(geometry.at.x + delta.x, geometry.at.y + delta.y))
            is Geometry.LineString ->
                Geometry.LineString(
                    geometry.vertices.map { Coordinate(it.x + delta.x, it.y + delta.y) },
                )
            is Geometry.Polygon ->
                Geometry.Polygon(
                    geometry.ring.map { Coordinate(it.x + delta.x, it.y + delta.y) },
                )
        }

    fun replaceVertex(
        polygon: Geometry.Polygon?,
        index: Int,
        delta: Coordinate,
    ): Geometry.Polygon? {
        if (polygon == null || index !in polygon.ring.indices) return null
        val newRing = polygon.ring.toMutableList()
        val orig = newRing[index]
        newRing[index] = Coordinate(orig.x + delta.x, orig.y + delta.y)
        if (index == 0) {
            newRing[newRing.lastIndex] = newRing[0]
        } else if (index == polygon.ring.lastIndex) {
            newRing[0] = newRing[newRing.lastIndex]
        }
        return Geometry.Polygon(newRing)
    }

    /**
     * Removes ring[index] from the polygon. Endpoints (index 0 and
     * ring.lastIndex) cannot be deleted directly because they carry
     * the shared closure coordinate; the user must reshape via drag
     * instead. Returns null if the deletion would leave fewer than
     * MINIMUM_POLYGON_RING_SIZE coordinates (i.e. the polygon would
     * no longer describe an area).
     */
    fun deleteVertex(
        polygon: Geometry.Polygon,
        index: Int,
    ): Geometry.Polygon? {
        if (index <= 0 || index >= polygon.ring.lastIndex) return null
        if (polygon.ring.size - 1 < MINIMUM_POLYGON_RING_SIZE) return null
        val newRing = polygon.ring.toMutableList().apply { removeAt(index) }
        return Geometry.Polygon(newRing)
    }

    /**
     * Inserts a new vertex at the midpoint of the edge starting at
     * ring[edgeStartIndex]. Returns null if edgeStartIndex is out of
     * range for a closed ring.
     */
    fun insertVertexAtEdgeMidpoint(
        polygon: Geometry.Polygon,
        edgeStartIndex: Int,
    ): Geometry.Polygon? {
        if (edgeStartIndex < 0 || edgeStartIndex >= polygon.ring.lastIndex) return null
        val a = polygon.ring[edgeStartIndex]
        val b = polygon.ring[edgeStartIndex + 1]
        val midpoint = Coordinate((a.x + b.x) / 2.0, (a.y + b.y) / 2.0)
        val newRing = polygon.ring.toMutableList().apply { add(edgeStartIndex + 1, midpoint) }
        return Geometry.Polygon(newRing)
    }
}
