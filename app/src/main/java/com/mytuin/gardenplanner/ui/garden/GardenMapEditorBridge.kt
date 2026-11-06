package com.mytuin.gardenplanner.ui.garden

import android.view.MotionEvent
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView

/**
 * Bridges MapLibre touch and gesture events to the editor state
 * (Step 9g).
 *
 * The four state suppliers (state / draw state / shapes / origin)
 * are bundled into EditorStateBundle rather than passed individually
 * so the handler signatures stay under LongParameterList and so that
 * 9h has a single place to add layer-visibility or filter suppliers
 * without touching every call site.
 */
object GardenMapEditorBridge {
    data class TapHandlers(
        val onSelect: (String) -> Unit,
        val onClearSelection: () -> Unit,
        val onAddDrawVertex: (Coordinate) -> Unit,
    )

    data class EditorStateBundle(
        val stateSupplier: () -> GardenMapEditUiState,
        val drawStateSupplier: () -> MapDrawState,
        val shapesSupplier: () -> List<MapShape>,
        val originSupplier: () -> Wgs84,
    )

    fun installTapHandler(
        map: MapLibreMap,
        bundle: EditorStateBundle,
        tapHandlers: TapHandlers,
    ) {
        map.addOnMapClickListener { latLng ->
            val state = bundle.stateSupplier()
            val drawState = bundle.drawStateSupplier()
            if (drawState.mode == MapEditMode.DRAW && state.selectedShapeId != null) {
                tapHandlers.onAddDrawVertex(
                    MetresToWgs84.toCoordinate(bundle.originSupplier(), Wgs84(latLng.latitude, latLng.longitude)),
                )
            } else {
                val hitId = hitTest(map, latLng)
                if (hitId != null) {
                    tapHandlers.onSelect(hitId)
                } else if (state.selectedShapeId != null) {
                    tapHandlers.onClearSelection()
                }
            }
            true
        }
    }

    fun installTouchHandler(
        mapView: MapView,
        map: MapLibreMap,
        controller: MapEditorController,
        bundle: EditorStateBundle,
        onSave: (MapShape, Geometry) -> Unit,
    ) {
        mapView.setOnTouchListener { _, event ->
            if (bundle.drawStateSupplier().mode == MapEditMode.DRAW) {
                return@setOnTouchListener false
            }
            val state = bundle.stateSupplier()
            val shapes = bundle.shapesSupplier()
            val selectionId = state.selectedShapeId
            when {
                controller.isDragging -> {
                    val shape = selectionId?.let { id -> shapes.firstOrNull { it.id == id } }
                    if (shape != null) {
                        handleActiveDrag(event, map, controller, shape, bundle.originSupplier(), onSave)
                    } else {
                        controller.cancel()
                        false
                    }
                }
                selectionId != null && event.action == MotionEvent.ACTION_DOWN -> {
                    val selected = shapes.firstOrNull { it.id == selectionId }
                    if (selected != null) {
                        handlePotentialDragStart(event, map, selectionId, controller, shapes, bundle.originSupplier())
                    } else {
                        false
                    }
                }
                else -> false
            }
        }
    }

    fun installLongPressHandler(
        map: MapLibreMap,
        bundle: EditorStateBundle,
        onGeometryReplace: (MapShape, Geometry) -> Unit,
    ) {
        map.addOnMapLongClickListener { latLng ->
            if (bundle.drawStateSupplier().mode == MapEditMode.DRAW) return@addOnMapLongClickListener false
            val selectedId = bundle.stateSupplier().selectedShapeId ?: return@addOnMapLongClickListener false
            val shape = bundle.shapesSupplier().firstOrNull { it.id == selectedId } ?: return@addOnMapLongClickListener false
            val polygon = shape.geometry as? Geometry.Polygon ?: return@addOnMapLongClickListener false
            val origin = bundle.originSupplier()
            val pixel = map.projection.toScreenLocation(latLng)
            val deleteIndex = findVertexNearPixels(map, polygon, origin, pixel.x, pixel.y)
            if (deleteIndex != null) {
                val deleted = MapEditorGeometryOps.deleteVertex(polygon, deleteIndex)
                if (deleted != null) {
                    onGeometryReplace(shape, deleted)
                }
                return@addOnMapLongClickListener true
            }
            val edgeIndex = findEdgeNearPixels(map, polygon, origin, pixel.x, pixel.y)
            if (edgeIndex != null) {
                val inserted = MapEditorGeometryOps.insertVertexAtEdgeMidpoint(polygon, edgeIndex)
                if (inserted != null) {
                    onGeometryReplace(shape, inserted)
                }
                return@addOnMapLongClickListener true
            }
            false
        }
    }

    private fun hitTest(
        map: MapLibreMap,
        latLng: LatLng,
    ): String? {
        val pixel = map.projection.toScreenLocation(latLng)
        val hits =
            map.queryRenderedFeatures(
                pixel,
                MapLibreStyleConstants.LAYER_GROWING_SPACES_FILL,
                MapLibreStyleConstants.LAYER_SPATIAL_OBJECTS_FILL,
            )
        return hits.firstOrNull()?.getStringProperty(MapLibreStyleConstants.PROP_SHAPE_ID)
    }

    private fun handlePotentialDragStart(
        event: MotionEvent,
        map: MapLibreMap,
        selectionId: String,
        controller: MapEditorController,
        shapes: List<MapShape>,
        origin: Wgs84,
    ): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return false
        val selected = shapes.firstOrNull { it.id == selectionId } ?: return false
        return beginDragForSelection(selected, event, map, controller, origin)
    }

    private fun beginDragForSelection(
        selected: MapShape,
        event: MotionEvent,
        map: MapLibreMap,
        controller: MapEditorController,
        origin: Wgs84,
    ): Boolean {
        val downPoint = map.projection.fromScreenLocation(android.graphics.PointF(event.x, event.y))
        val downMeters = MetresToWgs84.toCoordinate(origin, Wgs84(downPoint.latitude, downPoint.longitude))
        val vertexIndex = findVertexNearPixels(map, selected.geometry as? Geometry.Polygon, origin, event.x, event.y)
        if (vertexIndex != null) {
            controller.beginVertexDrag(vertexIndex, downMeters, selected.geometry)
            return true
        }
        if (isShapeBodyHit(map, selected, event)) {
            controller.beginShapeDrag(downMeters, selected.geometry)
            return true
        }
        return false
    }

    private fun isShapeBodyHit(
        map: MapLibreMap,
        selected: MapShape,
        event: MotionEvent,
    ): Boolean {
        val pixel = android.graphics.PointF(event.x, event.y)
        val hits =
            map.queryRenderedFeatures(
                pixel,
                MapLibreStyleConstants.LAYER_GROWING_SPACES_FILL,
                MapLibreStyleConstants.LAYER_SPATIAL_OBJECTS_FILL,
            )
        return hits.firstOrNull()?.getStringProperty(MapLibreStyleConstants.PROP_SHAPE_ID) == selected.id
    }

    private fun handleActiveDrag(
        event: MotionEvent,
        map: MapLibreMap,
        controller: MapEditorController,
        selected: MapShape,
        origin: Wgs84,
        onSave: (MapShape, Geometry) -> Unit,
    ): Boolean =
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                updateDragFromTouchEvent(event, map, origin, controller)
                true
            }
            MotionEvent.ACTION_UP -> {
                updateDragFromTouchEvent(event, map, origin, controller)
                val committed = controller.endDrag()
                if (committed != null && committed != selected.geometry) {
                    onSave(selected, committed)
                }
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                controller.cancel()
                true
            }
            else -> false
        }

    private fun updateDragFromTouchEvent(
        event: MotionEvent,
        map: MapLibreMap,
        origin: Wgs84,
        controller: MapEditorController,
    ) {
        val point = map.projection.fromScreenLocation(android.graphics.PointF(event.x, event.y))
        controller.updateDrag(MetresToWgs84.toCoordinate(origin, Wgs84(point.latitude, point.longitude)))
    }

    private fun findVertexNearPixels(
        map: MapLibreMap,
        polygon: Geometry.Polygon?,
        origin: Wgs84,
        downX: Float,
        downY: Float,
    ): Int? {
        if (polygon == null) return null
        var bestIndex: Int? = null
        var bestDistance = MapEditorLayers.VERTEX_HIT_RADIUS_PX
        polygon.ring.forEachIndexed { index, coord ->
            if (index == polygon.ring.lastIndex) return@forEachIndexed
            val wgs = MetresToWgs84.toWgs84(origin, coord)
            val vertexPixel = map.projection.toScreenLocation(LatLng(wgs.latitude, wgs.longitude))
            val dx = vertexPixel.x - downX
            val dy = vertexPixel.y - downY
            val dist = kotlin.math.sqrt(dx * dx + dy * dy)
            if (dist < bestDistance) {
                bestDistance = dist
                bestIndex = index
            }
        }
        return bestIndex
    }

    private fun findEdgeNearPixels(
        map: MapLibreMap,
        polygon: Geometry.Polygon,
        origin: Wgs84,
        downX: Float,
        downY: Float,
    ): Int? {
        var bestIndex: Int? = null
        var bestDistance = MapEditorLayers.EDGE_HIT_RADIUS_PX
        for (i in 0 until polygon.ring.lastIndex) {
            val a = screenPoint(map, polygon.ring[i], origin)
            val b = screenPoint(map, polygon.ring[i + 1], origin)
            val distance = distanceToSegment(downX, downY, a.x, a.y, b.x, b.y)
            if (distance < bestDistance) {
                bestDistance = distance
                bestIndex = i
            }
        }
        return bestIndex
    }

    private fun screenPoint(
        map: MapLibreMap,
        coordinate: Coordinate,
        origin: Wgs84,
    ): android.graphics.PointF {
        val wgs = MetresToWgs84.toWgs84(origin, coordinate)
        return map.projection.toScreenLocation(LatLng(wgs.latitude, wgs.longitude))
    }

    private fun distanceToSegment(
        px: Float,
        py: Float,
        ax: Float,
        ay: Float,
        bx: Float,
        by: Float,
    ): Float {
        val dx = bx - ax
        val dy = by - ay
        val lengthSquared = dx * dx + dy * dy
        if (lengthSquared == 0f) return kotlin.math.hypot(px - ax, py - ay)
        val t = (((px - ax) * dx + (py - ay) * dy) / lengthSquared).coerceIn(0f, 1f)
        val projX = ax + t * dx
        val projY = ay + t * dy
        return kotlin.math.hypot(px - projX, py - projY)
    }
}
