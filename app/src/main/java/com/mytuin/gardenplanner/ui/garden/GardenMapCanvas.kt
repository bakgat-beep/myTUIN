package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.ui.theme.Spacing
import kotlin.math.log2
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.sources.GeoJsonSource

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
    val session = rememberMapEditorSession(editState, uiState.shapes)
    val drawState = rememberMapDrawState()
    val selectedShape = editState.selectedShapeId?.let { id -> uiState.shapes.firstOrNull { it.id == id } }

    LaunchedEffect(selectedShape?.id) {
        drawState.exitDraw()
    }

    GardenMapContent(
        session = session,
        drawState = drawState,
        uiState = uiState,
        editState = editState,
        selectedShape = selectedShape,
        actions =
            GardenMapActions(
                onSelectShape = onSelectShape,
                onClearSelection = onClearSelection,
                onSaveGeometry = onSaveGeometry,
                onArchiveSelected = onArchiveSelected,
                onUndoSelected = onUndoSelected,
            ),
        modifier = modifier,
    )
}

private data class GardenMapActions(
    val onSelectShape: (String) -> Unit,
    val onClearSelection: () -> Unit,
    val onSaveGeometry: (MapShape, Geometry) -> Unit,
    val onArchiveSelected: (MapShape) -> Unit,
    val onUndoSelected: (MapShape) -> Unit,
)

private class MapEditorSession(
    val mapView: MapView,
) {
    val mapRef: MutableState<MapLibreMap?> = mutableStateOf(null)
    val styleRef: MutableState<Style?> = mutableStateOf(null)
    val styleVersion: MutableIntState = mutableIntStateOf(0)
    val recenterTrigger: MutableIntState = mutableIntStateOf(0)
    val controller: MapEditorController = MapEditorController()
    var stateRef: MutableState<GardenMapEditUiState> = mutableStateOf(GardenMapEditUiState())
    var shapesRef: MutableState<List<MapShape>> = mutableStateOf(emptyList())
}

@Composable
private fun rememberMapEditorSession(
    editState: GardenMapEditUiState,
    shapes: List<MapShape>,
): MapEditorSession {
    val context = LocalContext.current
    val session = remember(context) { MapEditorSession(MapView(context).also { it.onCreate(null) }) }
    LaunchedEffect(editState, shapes) {
        session.stateRef.value = editState
        session.shapesRef.value = shapes
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(session.mapView, lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> session.mapView.onStart()
                    Lifecycle.Event.ON_RESUME -> session.mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> session.mapView.onPause()
                    Lifecycle.Event.ON_STOP -> session.mapView.onStop()
                    else -> Unit
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            session.mapView.onDestroy()
        }
    }
    return session
}

@Composable
private fun GardenMapContent(
    session: MapEditorSession,
    drawState: MapDrawState,
    uiState: GardenMapUiState.Ready,
    editState: GardenMapEditUiState,
    selectedShape: MapShape?,
    actions: GardenMapActions,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        GardenMapAndroidView(
            mapView = session.mapView,
            originSupplier = { uiState.origin },
            session = session,
            drawStateSupplier = { drawState },
            actions = actions,
        )
        ShapeLayersEffect(
            styleRef = session.styleRef.value,
            styleVersion = session.styleVersion.intValue,
            shapes = uiState.shapes,
            origin = uiState.origin,
        )
        SelectionLayersEffect(
            styleRef = session.styleRef.value,
            styleVersion = session.styleVersion.intValue,
            selectedShape = selectedShape,
            mode = drawState.mode,
            controller = session.controller,
            origin = uiState.origin,
        )
        DrawPreviewEffect(
            styleRef = session.styleRef.value,
            styleVersion = session.styleVersion.intValue,
            drawState = drawState,
            origin = uiState.origin,
        )
        FitViewportEffect(
            mapRef = session.mapRef.value,
            styleVersion = session.styleVersion.intValue,
            viewport = uiState.viewport,
            origin = uiState.origin,
            recenterTrigger = session.recenterTrigger.intValue,
        )
        MapOverlayControls(
            selectedShape = selectedShape,
            drawState = drawState,
            isSaving = editState.isSaving,
            onRecenter = { session.recenterTrigger.intValue += 1 },
            onArchiveSelected = actions.onArchiveSelected,
            onUndoSelected = actions.onUndoSelected,
            onSaveGeometry = actions.onSaveGeometry,
        )
    }
}

@Composable
private fun GardenMapAndroidView(
    mapView: MapView,
    originSupplier: () -> Wgs84,
    session: MapEditorSession,
    drawStateSupplier: () -> MapDrawState,
    actions: GardenMapActions,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            mapView.apply {
                getMapAsync { map ->
                    session.mapRef.value = map
                    map.setStyle(MapLibreStyleConstants.STYLE_URL) { style ->
                        session.styleRef.value = style
                        GardenMapStyle.install(style)
                        MapEditorLayers.install(style)
                        session.styleVersion.intValue += 1
                    }
                    val bundle =
                        GardenMapEditorBridge.EditorStateBundle(
                            stateSupplier = { session.stateRef.value },
                            drawStateSupplier = drawStateSupplier,
                            shapesSupplier = { session.shapesRef.value },
                            originSupplier = originSupplier,
                        )
                    GardenMapEditorBridge.installTapHandler(
                        map = map,
                        bundle = bundle,
                        tapHandlers =
                            GardenMapEditorBridge.TapHandlers(
                                onSelect = actions.onSelectShape,
                                onClearSelection = actions.onClearSelection,
                                onAddDrawVertex = { drawStateSupplier().addVertex(it) },
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
            }
        },
    )
}

@Composable
private fun ShapeLayersEffect(
    styleRef: Style?,
    styleVersion: Int,
    shapes: List<MapShape>,
    origin: Wgs84,
) {
    LaunchedEffect(styleVersion, shapes) {
        if (styleVersion == 0) return@LaunchedEffect
        val style = styleRef ?: return@LaunchedEffect
        style
            .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_AREAS)
            ?.setGeoJson(GardenMapFeatures.areas(shapes, origin))
        style
            .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS)
            ?.setGeoJson(GardenMapFeatures.spatialObjects(shapes, origin))
        style
            .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_GROWING_SPACES)
            ?.setGeoJson(GardenMapFeatures.growingSpaces(shapes, origin))
    }
}

@Composable
private fun SelectionLayersEffect(
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
private fun DrawPreviewEffect(
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
private fun FitViewportEffect(
    mapRef: MapLibreMap?,
    styleVersion: Int,
    viewport: MapViewport,
    origin: Wgs84,
    recenterTrigger: Int,
) {
    LaunchedEffect(styleVersion, viewport, recenterTrigger) {
        if (styleVersion == 0) return@LaunchedEffect
        val map = mapRef ?: return@LaunchedEffect
        fitViewport(map, viewport, origin)
    }
}

@Composable
private fun BoxScope.MapOverlayControls(
    selectedShape: MapShape?,
    drawState: MapDrawState,
    isSaving: Boolean,
    onRecenter: () -> Unit,
    onArchiveSelected: (MapShape) -> Unit,
    onUndoSelected: (MapShape) -> Unit,
    onSaveGeometry: (MapShape, Geometry) -> Unit,
) {
    if (selectedShape != null) {
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
    FloatingActionButton(
        onClick = onRecenter,
        modifier =
            Modifier
                .align(Alignment.BottomEnd)
                .padding(Spacing.l),
    ) {
        Icon(
            imageVector = Icons.Filled.MyLocation,
            contentDescription = stringResource(R.string.garden_map_recentre_action),
        )
    }
}

private const val FALLBACK_ZOOM = 17.0
private const val ZOOM_SCALE_BASE = 17.0
private const val MIN_ZOOM = 12.0
private const val MAX_ZOOM = 20.0

private fun fitViewport(
    map: MapLibreMap,
    viewport: MapViewport,
    origin: Wgs84,
) {
    val centreWgs =
        MetresToWgs84.toWgs84(
            origin,
            Coordinate(viewport.centerX, viewport.centerY),
        )
    val largestDimension = viewport.largestDimensionMetres
    val zoom =
        if (largestDimension <= 0.0) {
            FALLBACK_ZOOM
        } else {
            (ZOOM_SCALE_BASE - log2(largestDimension)).coerceIn(MIN_ZOOM, MAX_ZOOM)
        }
    map.cameraPosition =
        CameraPosition
            .Builder()
            .target(LatLng(centreWgs.latitude, centreWgs.longitude))
            .zoom(zoom)
            .build()
}
