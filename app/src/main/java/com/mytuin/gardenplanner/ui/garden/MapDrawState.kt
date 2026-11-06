package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry

internal const val MIN_POLYGON_VERTEX_COUNT = 3

@Stable
class MapDrawState(
    initialMode: MapEditMode = MapEditMode.SELECT,
    initialVertices: List<Coordinate> = emptyList(),
) {
    var mode by mutableStateOf(initialMode)
        private set

    var vertices by mutableStateOf(initialVertices)
        private set

    fun enterDraw() {
        mode = MapEditMode.DRAW
        vertices = emptyList()
    }

    fun exitDraw() {
        mode = MapEditMode.SELECT
        vertices = emptyList()
    }

    fun addVertex(coordinate: Coordinate) {
        if (mode == MapEditMode.DRAW) {
            vertices = vertices + coordinate
        }
    }

    fun clearVertices() {
        vertices = emptyList()
    }

    fun buildPolygon(): Geometry.Polygon? {
        if (vertices.size < MIN_POLYGON_VERTEX_COUNT) return null
        val closed = vertices + vertices.first()
        return Geometry.Polygon(closed)
    }
}

private val MapDrawStateSaver =
    listSaver<MapDrawState, String>(
        save = { state ->
            listOf(
                state.mode.name,
                state.vertices.joinToString(";") { "${it.x},${it.y}" },
            )
        },
        restore = { saved ->
            MapDrawState(
                initialMode = MapEditMode.valueOf(saved[0]),
                initialVertices = parseVertices(saved.getOrNull(1).orEmpty()),
            )
        },
    )

private fun parseVertices(raw: String): List<Coordinate> {
    if (raw.isEmpty()) return emptyList()
    return raw.split(";").mapNotNull { pair ->
        val parts = pair.split(",")
        if (parts.size != 2) return@mapNotNull null
        val x = parts[0].toDoubleOrNull() ?: return@mapNotNull null
        val y = parts[1].toDoubleOrNull() ?: return@mapNotNull null
        Coordinate(x, y)
    }
}

@Composable
fun rememberMapDrawState(): MapDrawState = rememberSaveable(saver = MapDrawStateSaver) { MapDrawState() }
