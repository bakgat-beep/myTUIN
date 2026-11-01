package com.mytuin.gardenplanner.ui.garden

import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.FeatureCollection

/**
 * Style constants and layer setup for the Garden map (Step 9f).
 *
 * The style URL is the single point of basemap choice. Setting it to
 * null disables the basemap entirely and renders shapes on a neutral
 * background — that is the offline-safe mode. See DEC-050.
 */
object MapLibreStyleConstants {
    const val STYLE_URL: String = "https://demotiles.maplibre.org/style.json"

    const val SOURCE_AREAS = "garden-areas"
    const val SOURCE_SPATIAL_OBJECTS = "garden-spatial-objects"
    const val SOURCE_GROWING_SPACES = "garden-growing-spaces"

    const val LAYER_AREAS_FILL = "garden-areas-fill"
    const val LAYER_AREAS_LINE = "garden-areas-line"
    const val LAYER_SPATIAL_OBJECTS_FILL = "garden-spatial-objects-fill"
    const val LAYER_SPATIAL_OBJECTS_LINE = "garden-spatial-objects-line"
    const val LAYER_SPATIAL_OBJECTS_CIRCLE = "garden-spatial-objects-circle"
    const val LAYER_GROWING_SPACES_FILL = "garden-growing-spaces-fill"
    const val LAYER_GROWING_SPACES_LINE = "garden-growing-spaces-line"
    const val LAYER_GROWING_SPACES_CIRCLE = "garden-growing-spaces-circle"

    // GeoJSON feature property names. Feature properties carry the
    // colour and opacity values looked up by MapShapeStyle.
    const val PROP_FILL_COLOR = "fillColor"
    const val PROP_FILL_OPACITY = "fillOpacity"
    const val PROP_STROKE_COLOR = "strokeColor"
    const val PROP_STROKE_WIDTH = "strokeWidth"
}

private const val CIRCLE_RADIUS_PX = 5f
private const val CIRCLE_STROKE_WIDTH_PX = 2f

object GardenMapStyle {
    /**
     * Called from within MapLibre's setStyle callback. Adds the three
     * sources with empty feature collections and the eight layers
     * that draw them. Layer order is bottom-to-top:
     *
     *   areas fill → areas line
     *   spatial objects fill → line → circle
     *   growing spaces fill → line → circle
     *
     * Feature properties drive colour and stroke via expressions so
     * the individual MapShape style choices live in MapShapeStyle,
     * not in this file.
     */
    fun install(style: Style) {
        style.addSource(GeoJsonSource(MapLibreStyleConstants.SOURCE_AREAS, FeatureCollection.fromFeatures(emptyList())))
        style.addSource(
            GeoJsonSource(MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS, FeatureCollection.fromFeatures(emptyList())),
        )
        style.addSource(
            GeoJsonSource(MapLibreStyleConstants.SOURCE_GROWING_SPACES, FeatureCollection.fromFeatures(emptyList())),
        )

        style.addLayer(areasFill())
        style.addLayer(areasLine())
        style.addLayer(spatialObjectsFill())
        style.addLayer(spatialObjectsLine())
        style.addLayer(spatialObjectsCircle())
        style.addLayer(growingSpacesFill())
        style.addLayer(growingSpacesLine())
        style.addLayer(growingSpacesCircle())
    }

    private fun areasFill() =
        FillLayer(MapLibreStyleConstants.LAYER_AREAS_FILL, MapLibreStyleConstants.SOURCE_AREAS)
            .withProperties(
                PropertyFactory.fillColor(Expression.get(MapLibreStyleConstants.PROP_FILL_COLOR)),
                PropertyFactory.fillOpacity(Expression.get(MapLibreStyleConstants.PROP_FILL_OPACITY)),
            )

    private fun areasLine() =
        LineLayer(MapLibreStyleConstants.LAYER_AREAS_LINE, MapLibreStyleConstants.SOURCE_AREAS)
            .withProperties(
                PropertyFactory.lineColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
                PropertyFactory.lineWidth(Expression.get(MapLibreStyleConstants.PROP_STROKE_WIDTH)),
            )

    private fun spatialObjectsFill() =
        FillLayer(MapLibreStyleConstants.LAYER_SPATIAL_OBJECTS_FILL, MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS)
            .withProperties(
                PropertyFactory.fillColor(Expression.get(MapLibreStyleConstants.PROP_FILL_COLOR)),
                PropertyFactory.fillOpacity(Expression.get(MapLibreStyleConstants.PROP_FILL_OPACITY)),
                PropertyFactory.fillOutlineColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
            )

    private fun spatialObjectsLine() =
        LineLayer(MapLibreStyleConstants.LAYER_SPATIAL_OBJECTS_LINE, MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS)
            .withProperties(
                PropertyFactory.lineColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
                PropertyFactory.lineWidth(Expression.get(MapLibreStyleConstants.PROP_STROKE_WIDTH)),
            )

    private fun spatialObjectsCircle() =
        CircleLayer(MapLibreStyleConstants.LAYER_SPATIAL_OBJECTS_CIRCLE, MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS)
            .withProperties(
                PropertyFactory.circleColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
                PropertyFactory.circleRadius(CIRCLE_RADIUS_PX),
                PropertyFactory.circleStrokeColor("#FAF9F5"),
                PropertyFactory.circleStrokeWidth(CIRCLE_STROKE_WIDTH_PX),
            )

    private fun growingSpacesFill() =
        FillLayer(MapLibreStyleConstants.LAYER_GROWING_SPACES_FILL, MapLibreStyleConstants.SOURCE_GROWING_SPACES)
            .withProperties(
                PropertyFactory.fillColor(Expression.get(MapLibreStyleConstants.PROP_FILL_COLOR)),
                PropertyFactory.fillOpacity(Expression.get(MapLibreStyleConstants.PROP_FILL_OPACITY)),
                PropertyFactory.fillOutlineColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
            )

    private fun growingSpacesLine() =
        LineLayer(MapLibreStyleConstants.LAYER_GROWING_SPACES_LINE, MapLibreStyleConstants.SOURCE_GROWING_SPACES)
            .withProperties(
                PropertyFactory.lineColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
                PropertyFactory.lineWidth(Expression.get(MapLibreStyleConstants.PROP_STROKE_WIDTH)),
            )

    private fun growingSpacesCircle() =
        CircleLayer(MapLibreStyleConstants.LAYER_GROWING_SPACES_CIRCLE, MapLibreStyleConstants.SOURCE_GROWING_SPACES)
            .withProperties(
                PropertyFactory.circleColor(Expression.get(MapLibreStyleConstants.PROP_STROKE_COLOR)),
                PropertyFactory.circleRadius(CIRCLE_RADIUS_PX),
                PropertyFactory.circleStrokeColor("#FAF9F5"),
                PropertyFactory.circleStrokeWidth(CIRCLE_STROKE_WIDTH_PX),
            )
}
