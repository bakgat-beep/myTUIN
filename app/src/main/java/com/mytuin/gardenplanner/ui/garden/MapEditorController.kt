package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry

/**
 * Transient drag state for the map editor (Step 9g).
 *
 * Lives for the duration of one gesture. Created with `remember`
 * (not `rememberSaveable`): a drag in progress across rotation
 * cannot meaningfully resume, so this state is deliberately not
 * persisted. The one exception — draw-mode vertices — will be
 * handled separately in Round 2b when Draw mode lands.
 *
 * The ViewModel owns durable state (selection, edit mode, save
 * status). This controller owns only what exists between
 * ACTION_DOWN and ACTION_UP.
 */
@Stable
class MapEditorController {
    var dragVertexIndex by mutableStateOf<Int?>(null)
        private set
    var dragStartMeters by mutableStateOf<Coordinate?>(null)
        private set
    var dragCurrentMeters by mutableStateOf<Coordinate?>(null)
        private set
    var dragOriginalGeometry by mutableStateOf<Geometry?>(null)
        private set

    val isDragging: Boolean
        get() = dragOriginalGeometry != null

    fun beginShapeDrag(
        startMeters: Coordinate,
        originalGeometry: Geometry,
    ) {
        dragVertexIndex = null
        dragStartMeters = startMeters
        dragCurrentMeters = startMeters
        dragOriginalGeometry = originalGeometry
    }

    fun beginVertexDrag(
        index: Int,
        startMeters: Coordinate,
        originalGeometry: Geometry,
    ) {
        dragVertexIndex = index
        dragStartMeters = startMeters
        dragCurrentMeters = startMeters
        dragOriginalGeometry = originalGeometry
    }

    fun updateDrag(currentMeters: Coordinate) {
        if (isDragging) {
            dragCurrentMeters = currentMeters
        }
    }

    /**
     * Ends the drag and returns the final geometry (with the drag
     * delta applied), or null if nothing was being dragged.
     * Resets all transient state either way.
     */
    fun endDrag(): Geometry? {
        val original = dragOriginalGeometry
        val start = dragStartMeters
        val current = dragCurrentMeters
        val vertexIndex = dragVertexIndex
        val result =
            if (original != null && start != null && current != null) {
                val delta = Coordinate(current.x - start.x, current.y - start.y)
                if (vertexIndex != null) {
                    MapEditorGeometryOps.replaceVertex(original as? Geometry.Polygon, vertexIndex, delta)
                } else {
                    MapEditorGeometryOps.translate(original, delta)
                }
            } else {
                null
            }
        cancel()
        return result
    }

    fun cancel() {
        dragVertexIndex = null
        dragStartMeters = null
        dragCurrentMeters = null
        dragOriginalGeometry = null
    }

    /**
     * Live preview of the geometry with the current drag delta
     * applied. Returns null when no drag is in progress. Used to
     * render the selection layer while dragging.
     */
    fun inProgressGeometry(): Geometry? {
        val original = dragOriginalGeometry ?: return null
        val start = dragStartMeters ?: return null
        val current = dragCurrentMeters ?: return null
        val delta = Coordinate(current.x - start.x, current.y - start.y)
        return if (dragVertexIndex != null) {
            MapEditorGeometryOps.replaceVertex(original as? Geometry.Polygon, dragVertexIndex!!, delta)
        } else {
            MapEditorGeometryOps.translate(original, delta)
        }
    }
}
