package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.LineString
import org.maplibre.geojson.Point
import org.maplibre.geojson.Polygon

/**
 * FeatureCollection builders for the Garden map (Step 9f).
 *
 * Converts each MapShape into a MapLibre Feature with the style
 * properties that MapShapeStyle chose. Grouped by MapShapeKind so
 * each source receives only its own shapes and the layer stack keeps
 * the draw order the design intends (areas under, growing spaces on
 * top).
 *
 * The metre → WGS84 conversion happens here, using the garden
 * origin. It is the only place MapLibre types enter the codebase
 * alongside GardenMapStyle.
 */
object GardenMapFeatures {
    fun areas(
        shapes: List<MapShape>,
        origin: Wgs84,
    ): FeatureCollection = collection(shapes.filter { it.kind is MapShapeKind.Area }, origin)

    fun spatialObjects(
        shapes: List<MapShape>,
        origin: Wgs84,
    ): FeatureCollection = collection(shapes.filter { it.kind is MapShapeKind.SpatialObject }, origin)

    fun growingSpaces(
        shapes: List<MapShape>,
        origin: Wgs84,
    ): FeatureCollection = collection(shapes.filter { it.kind is MapShapeKind.GrowingSpace }, origin)

    private fun collection(
        shapes: List<MapShape>,
        origin: Wgs84,
    ): FeatureCollection {
        val features = shapes.mapNotNull { shape -> feature(shape, origin) }
        return FeatureCollection.fromFeatures(features)
    }

    private fun feature(
        shape: MapShape,
        origin: Wgs84,
    ): Feature? {
        val style = MapShapeStyle.forKind(shape.kind)
        val geometry =
            when (val g = shape.geometry) {
                is Geometry.Point -> point(g.at, origin)
                is Geometry.LineString -> line(g.vertices, origin)
                is Geometry.Polygon -> polygon(g.ring, origin)
            }
        return Feature
            .fromGeometry(geometry)
            .apply {
                addStringProperty(MapLibreStyleConstants.PROP_FILL_COLOR, style.fillColor)
                addNumberProperty(MapLibreStyleConstants.PROP_FILL_OPACITY, style.fillOpacity.toDouble())
                addStringProperty(MapLibreStyleConstants.PROP_STROKE_COLOR, style.strokeColor)
                addNumberProperty(MapLibreStyleConstants.PROP_STROKE_WIDTH, style.strokeWidth.toDouble())
            }
    }

    private fun point(
        coordinate: Coordinate,
        origin: Wgs84,
    ): Point {
        val wgs = MetresToWgs84.toWgs84(origin, coordinate)
        return Point.fromLngLat(wgs.longitude, wgs.latitude)
    }

    private fun line(
        vertices: List<Coordinate>,
        origin: Wgs84,
    ): LineString {
        val points = vertices.map { point(it, origin) }
        return LineString.fromLngLats(points)
    }

    private fun polygon(
        ring: List<Coordinate>,
        origin: Wgs84,
    ): Polygon {
        val points = ring.map { point(it, origin) }
        return Polygon.fromLngLats(listOf(points))
    }
}
