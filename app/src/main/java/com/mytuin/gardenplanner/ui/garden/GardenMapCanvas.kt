package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.ui.theme.Spacing
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.sources.GeoJsonSource

/**
 * Interactive garden map (Steps 9f–9h) with edit overlays.
 *
 * The base map (shapes, viewport, recentre FAB, lifecycle) lives in
 * GardenShapesCanvas; this composable adds the editor-specific
 * layers, gesture handlers, and the edit control panel.
 *
 * Editor handler installation is extracted into a private top-level
 * function so the composable stays under LongMethod.
 */
@Composable
fun GardenMapCanvas(
    uiState: GardenMapUiState.Ready,
    editState: GardenMapEditUiState,
    onSelectShape: (String) -> Unit,
    onClearSelection: () -> Unit,
    onSaveGeometry: (MapShape, Geometry) -> Unit,
    onArchiveSelected: (MapShape) -> Unit,
    onUndoSelected: (MapShape) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shapesHandle = rememberShapesCanvasHandle()
    val editorSession = rememberEditorSession(editState, uiState.shapes)
    val drawState = rememberMapDrawState()
    val selectedShape = editState.selectedShapeId?.let { id -> uiState.shapes.firstOrNull { it.id == id } }
    val actions = MapEditorActions(onSelectShape, onClearSelection, onSaveGeometry)

    LaunchedEffect(selectedShape?.id) {
        drawState.exitDraw()
    }

    GardenShapesCanvas(
        handle = shapesHandle,
        shapes = uiState.shapes,
        viewport = uiState.viewport,
        origin = uiState.origin,
        modifier = modifier,
        onMapReady = { map, mapView, style ->
            installEditorHandlers(
                map = map,
                mapView = mapView,
                style = style,
                session = editorSession,
                drawState = drawState,
                origin = uiState.origin,
                actions = actions,
            )
        },
    ) {
        EditorSelectionEffect(
            styleRef = shapesHandle.currentStyle,
            styleVersion = shapesHandle.currentStyleVersion,
            selectedShape = selectedShape,
            mode = drawState.mode,
            controller = editorSession.controller,
            origin = uiState.origin,
        )
        EditorDrawPreviewEffect(
            styleRef = shapesHandle.currentStyle,
            styleVersion = shapesHandle.currentStyleVersion,
            drawState = drawState,
            origin = uiState.origin,
        )
        EditorControls(
            selectedShape = selectedShape,
            drawState = drawState,
            isSaving = editState.isSaving,
            onArchiveSelected = onArchiveSelected,
            onUndoSelected = onUndoSelected,
            onSaveGeometry = onSaveGeometry,
        )
    }
}

private data class MapEditorActions(
    val onSelectShape: (String) -> Unit,
    val onClearSelection: () -> Unit,
    val onSaveGeometry: (MapShape, Geometry) -> Unit,
)

private fun installEditorHandlers(
    map: MapLibreMap,
    mapView: MapView,
    style: Style,
    session: EditorSession,
    drawState: MapDrawState,
    origin: Wgs84,
    actions: MapEditorActions,
) {
    MapEditorLayers.install(style)
    val bundle =
        GardenMapEditorBridge.EditorStateBundle(
            stateSupplier = { session.stateRef.value },
            drawStateSupplier = { drawState },
            shapesSupplier = { session.shapesRef.value },
            originSupplier = { origin },
        )
    GardenMapEditorBridge.installTapHandler(
        map = map,
        bundle = bundle,
        tapHandlers =
            GardenMapEditorBridge.TapHandlers(
                onSelect = actions.onSelectShape,
                onClearSelection = actions.onClearSelection,
                onAddDrawVertex = drawState::addVertex,
            ),
    )
    GardenMapEditorBridge.installTouchHandler(
        mapView = mapView,
        map = map,
        controller = session.controller,
        bundle = bundle,
        onSave = actions.onSaveGeometry,
    )
    GardenMapEditorBridge.installLongPressHandler(
        map = map,
        bundle = bundle,
        onGeometryReplace = actions.onSaveGeometry,
    )
}

private class EditorSession(
    val controller: MapEditorController,
) {
    var stateRef: MutableState<GardenMapEditUiState> = mutableStateOf(GardenMapEditUiState())
    var shapesRef: MutableState<List<MapShape>> = mutableStateOf(emptyList())
}

@Composable
private fun rememberEditorSession(
    editState: GardenMapEditUiState,
    shapes: List<MapShape>,
): EditorSession {
    val session = remember { EditorSession(MapEditorController()) }
    LaunchedEffect(editState, shapes) {
        session.stateRef.value = editState
        session.shapesRef.value = shapes
    }
    return session
}

@Composable
private fun EditorSelectionEffect(
    styleRef: Style?,
    styleVersion: Int,
    selectedShape: MapShape?,
    mode: MapEditMode,
    controller: MapEditorController,
    origin: Wgs84,
) {
    LaunchedEffect(
        styleVersion,
        selectedShape?.id,
        mode,
        controller.isDragging,
        controller.dragCurrentMeters,
    ) {
        if (styleVersion == 0) return@LaunchedEffect
        val style = styleRef ?: return@LaunchedEffect
        val geometry = controller.inProgressGeometry() ?: selectedShape?.geometry
        if (geometry != null) {
            style
                .getSourceAs<GeoJsonSource>(MapEditorLayers.SOURCE_SELECTION)
                ?.setGeoJson(MapEditorFeatures.selection(geometry, origin))
        } else {
            style
                .getSourceAs<GeoJsonSource>(MapEditorLayers.SOURCE_SELECTION)
                ?.setGeoJson(MapEditorFeatures.empty())
        }
        val showVertices = mode == MapEditMode.SELECT && !controller.isDragging && geometry is Geometry.Polygon
        val vertexCollection =
            if (showVertices) {
                MapEditorFeatures.vertices(geometry as Geometry.Polygon, origin)
            } else {
                MapEditorFeatures.empty()
            }
        style
            .getSourceAs<GeoJsonSource>(MapEditorLayers.SOURCE_VERTICES)
            ?.setGeoJson(vertexCollection)
    }
}

@Composable
private fun EditorDrawPreviewEffect(
    styleRef: Style?,
    styleVersion: Int,
    drawState: MapDrawState,
    origin: Wgs84,
) {
    LaunchedEffect(styleVersion, drawState.vertices) {
        if (styleVersion == 0) return@LaunchedEffect
        val style = styleRef ?: return@LaunchedEffect
        val vertices = drawState.vertices
        val lineCollection =
            if (vertices.size >= 2) {
                MapEditorFeatures.drawPreview(vertices, origin)
            } else {
                MapEditorFeatures.empty()
            }
        style
            .getSourceAs<GeoJsonSource>(MapEditorLayers.SOURCE_DRAW_LINE)
            ?.setGeoJson(lineCollection)
        style
            .getSourceAs<GeoJsonSource>(MapEditorLayers.SOURCE_DRAW_VERTICES)
            ?.setGeoJson(MapEditorFeatures.drawVertices(vertices, origin))
    }
}

@Composable
private fun BoxScope.EditorControls(
    selectedShape: MapShape?,
    drawState: MapDrawState,
    isSaving: Boolean,
    onArchiveSelected: (MapShape) -> Unit,
    onUndoSelected: (MapShape) -> Unit,
    onSaveGeometry: (MapShape, Geometry) -> Unit,
) {
    if (selectedShape == null) return
    MapEditorControlPanel(
        mode = drawState.mode,
        isSaving = isSaving,
        canCloseRing = drawState.vertices.size >= MIN_POLYGON_VERTEX_COUNT,
        callbacks =
            MapEditorCallbacks(
                onModeChange = { mode ->
                    when (mode) {
                        MapEditMode.SELECT -> drawState.exitDraw()
                        MapEditMode.DRAW -> drawState.enterDraw()
                    }
                },
                onDelete = { onArchiveSelected(selectedShape) },
                onUndo = { onUndoSelected(selectedShape) },
                onCloseRing = {
                    val polygon = drawState.buildPolygon()
                    if (polygon != null) {
                        onSaveGeometry(selectedShape, polygon)
                        drawState.exitDraw()
                    }
                },
                onClearVertices = drawState::clearVertices,
                onCancelDraw = drawState::exitDraw,
            ),
        modifier =
            Modifier
                .align(Alignment.TopCenter)
                .padding(Spacing.l),
    )
}
