package com.mytuin.gardenplanner.ui.garden

import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.FeatureCollection

/**
 * Selection, vertex and draw-mode layers (Step 9g).
 *
 * Layered on top of the shape layers installed by GardenMapStyle so
 * highlights paint over the base shape rendering.
 */
object MapEditorLayers {
    const val SOURCE_SELECTION = "editor-selection"
    const val SOURCE_VERTICES = "editor-vertices"
    const val SOURCE_DRAW_LINE = "editor-draw-line"
    const val SOURCE_DRAW_VERTICES = "editor-draw-vertices"

    const val LAYER_SELECTION_FILL = "editor-selection-fill"
    const val LAYER_SELECTION_LINE = "editor-selection-line"
    const val LAYER_VERTICES_CIRCLE = "editor-vertices-circle"
    const val LAYER_DRAW_LINE = "editor-draw-line"
    const val LAYER_DRAW_VERTICES_CIRCLE = "editor-draw-vertices-circle"

    private const val SELECTION_FILL_COLOR = "#B4863A"
    private const val SELECTION_FILL_OPACITY = 0.15f
    private const val SELECTION_STROKE_COLOR = "#B4863A"
    private const val SELECTION_STROKE_WIDTH_PX = 3f

    private const val VERTEX_FILL_COLOR = "#B4863A"
    private const val VERTEX_RADIUS_PX = 8f
    private const val VERTEX_STROKE_COLOR = "#FAF9F5"
    private const val VERTEX_STROKE_WIDTH_PX = 2f

    private const val DRAW_LINE_COLOR = "#60788A"
    private const val DRAW_LINE_WIDTH_PX = 2.5f
    private const val DRAW_LINE_DASH_PRIMARY_PX = 2f
    private const val DRAW_LINE_DASH_SECONDARY_PX = 2f

    private const val DRAW_VERTEX_FILL_COLOR = "#60788A"
    private const val DRAW_VERTEX_RADIUS_PX = 7f
    private const val DRAW_VERTEX_STROKE_COLOR = "#FAF9F5"
    private const val DRAW_VERTEX_STROKE_WIDTH_PX = 2f

    const val VERTEX_HIT_RADIUS_PX = 24f
    const val EDGE_HIT_RADIUS_PX = 24f

    fun install(style: Style) {
        style.addSource(GeoJsonSource(SOURCE_SELECTION, FeatureCollection.fromFeatures(emptyList())))
        style.addSource(GeoJsonSource(SOURCE_VERTICES, FeatureCollection.fromFeatures(emptyList())))
        style.addSource(GeoJsonSource(SOURCE_DRAW_LINE, FeatureCollection.fromFeatures(emptyList())))
        style.addSource(GeoJsonSource(SOURCE_DRAW_VERTICES, FeatureCollection.fromFeatures(emptyList())))

        style.addLayer(
            FillLayer(LAYER_SELECTION_FILL, SOURCE_SELECTION).withProperties(
                PropertyFactory.fillColor(SELECTION_FILL_COLOR),
                PropertyFactory.fillOpacity(SELECTION_FILL_OPACITY),
            ),
        )
        style.addLayer(
            LineLayer(LAYER_SELECTION_LINE, SOURCE_SELECTION).withProperties(
                PropertyFactory.lineColor(SELECTION_STROKE_COLOR),
                PropertyFactory.lineWidth(SELECTION_STROKE_WIDTH_PX),
            ),
        )
        style.addLayer(
            CircleLayer(LAYER_VERTICES_CIRCLE, SOURCE_VERTICES).withProperties(
                PropertyFactory.circleColor(VERTEX_FILL_COLOR),
                PropertyFactory.circleRadius(VERTEX_RADIUS_PX),
                PropertyFactory.circleStrokeColor(VERTEX_STROKE_COLOR),
                PropertyFactory.circleStrokeWidth(VERTEX_STROKE_WIDTH_PX),
            ),
        )
        style.addLayer(
            LineLayer(LAYER_DRAW_LINE, SOURCE_DRAW_LINE).withProperties(
                PropertyFactory.lineColor(DRAW_LINE_COLOR),
                PropertyFactory.lineWidth(DRAW_LINE_WIDTH_PX),
                PropertyFactory.lineDasharray(
                    arrayOf(DRAW_LINE_DASH_PRIMARY_PX, DRAW_LINE_DASH_SECONDARY_PX),
                ),
            ),
        )
        style.addLayer(
            CircleLayer(LAYER_DRAW_VERTICES_CIRCLE, SOURCE_DRAW_VERTICES).withProperties(
                PropertyFactory.circleColor(DRAW_VERTEX_FILL_COLOR),
                PropertyFactory.circleRadius(DRAW_VERTEX_RADIUS_PX),
                PropertyFactory.circleStrokeColor(DRAW_VERTEX_STROKE_COLOR),
                PropertyFactory.circleStrokeWidth(DRAW_VERTEX_STROKE_WIDTH_PX),
            ),
        )
    }
}
