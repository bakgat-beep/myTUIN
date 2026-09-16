package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

/**
 * Geometry in the garden's local spatial frame (A50b=(i)).
 *
 * DEC-039: technology-independent. No dependency on MapLibre types.
 * A50=GeoJSON: the on-disk representation is GeoJSON (RFC 7946),
 * serialised by GeometryGeoJson in the data layer. The domain model
 * is a sealed hierarchy; serialisation is a storage concern.
 *
 * V1 supports point, line and polygon (GARDEN_VOCABULARIES §7).
 * Polygons are single-ring; holes are not modelled (see step 5c notes).
 *
 * Invariants are enforced in constructors so a malformed geometry
 * cannot exist inside the domain.
 */
sealed interface Geometry {

    val geometryType: GeometryType

    data class Point(val at: Coordinate) : Geometry {
        override val geometryType: GeometryType = GeometryType.POINT
    }

    data class LineString(val vertices: List<Coordinate>) : Geometry {
        override val geometryType: GeometryType = GeometryType.LINE

        init {
            require(vertices.size >= 2) {
                "LineString requires at least 2 vertices; got ${vertices.size}"
            }
        }
    }

    data class Polygon(val ring: List<Coordinate>) : Geometry {
        override val geometryType: GeometryType = GeometryType.POLYGON

        init {
            require(ring.size >= 4) {
                "Polygon ring requires at least 4 coordinates (closed); got ${ring.size}"
            }
            require(ring.first() == ring.last()) {
                "Polygon ring must be closed: first coordinate must equal last"
            }
        }
    }
}