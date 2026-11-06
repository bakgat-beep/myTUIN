package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
import org.maplibre.geojson.Polygon

/**
 * FeatureCollection builders for the editor layers (Step 9g).
 */
object MapEditorFeatures {
    fun selection(
        geometry: Geometry,
        origin: Wgs84,
    ): FeatureCollection {
        val feature =
            when (geometry) {
                is Geometry.Point -> Feature.fromGeometry(vertexToPoint(geometry.at, origin))
                is Geometry.LineString ->
                    Feature.fromGeometry(
                        LineString.fromLngLats(geometry.vertices.map { vertexToPoint(it, origin) }),
                    )
                is Geometry.Polygon ->
                    Feature.fromGeometry(
                        Polygon.fromLngLats(listOf(geometry.ring.map { vertexToPoint(it, origin) })),
                    )
            }
        return FeatureCollection.fromFeatures(listOf(feature))
    }

    fun vertices(
        polygon: Geometry.Polygon,
        origin: Wgs84,
    ): FeatureCollection {
        val features = polygon.ring.map { vertex -> Feature.fromGeometry(vertexToPoint(vertex, origin)) }
        return FeatureCollection.fromFeatures(features)
    }

    fun drawPreview(
        vertices: List<Coordinate>,
        origin: Wgs84,
    ): FeatureCollection {
        if (vertices.size < 2) return empty()
        val points = vertices.map { vertexToPoint(it, origin) }
        return FeatureCollection.fromFeatures(
            listOf(Feature.fromGeometry(LineString.fromLngLats(points))),
        )
    }

    fun drawVertices(
        vertices: List<Coordinate>,
        origin: Wgs84,
    ): FeatureCollection {
        val features = vertices.map { Feature.fromGeometry(vertexToPoint(it, origin)) }
        return FeatureCollection.fromFeatures(features)
    }

    fun empty(): FeatureCollection = FeatureCollection.fromFeatures(emptyList<Feature>())

    private fun vertexToPoint(
        coordinate: Coordinate,
        origin: Wgs84,
    ): Point {
        val wgs = MetresToWgs84.toWgs84(origin, coordinate)
        return Point.fromLngLat(wgs.longitude, wgs.latitude)
    }
}
