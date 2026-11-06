package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MapDrawStateTest {
    @Test
    fun `initial state is select mode with no vertices`() {
        val state = MapDrawState()
        assertEquals(MapEditMode.SELECT, state.mode)
        assertTrue(state.vertices.isEmpty())
    }

    @Test
    fun `enterDraw sets mode to DRAW and clears vertices`() {
        val state = MapDrawState()
        state.enterDraw()
        assertEquals(MapEditMode.DRAW, state.mode)
        assertTrue(state.vertices.isEmpty())
    }

    @Test
    fun `exitDraw returns to SELECT and clears vertices`() {
        val state = MapDrawState()
        state.enterDraw()
        state.addVertex(Coordinate(0.0, 0.0))
        state.exitDraw()
        assertEquals(MapEditMode.SELECT, state.mode)
        assertTrue(state.vertices.isEmpty())
    }

    @Test
    fun `addVertex is a no-op when not in DRAW mode`() {
        val state = MapDrawState()
        state.addVertex(Coordinate(1.0, 2.0))
        assertTrue(state.vertices.isEmpty())
    }

    @Test
    fun `addVertex appends in DRAW mode`() {
        val state = MapDrawState()
        state.enterDraw()
        state.addVertex(Coordinate(1.0, 2.0))
        state.addVertex(Coordinate(3.0, 4.0))
        assertEquals(listOf(Coordinate(1.0, 2.0), Coordinate(3.0, 4.0)), state.vertices)
    }

    @Test
    fun `clearVertices empties without changing mode`() {
        val state = MapDrawState()
        state.enterDraw()
        state.addVertex(Coordinate(0.0, 0.0))
        state.clearVertices()
        assertTrue(state.vertices.isEmpty())
        assertEquals(MapEditMode.DRAW, state.mode)
    }

    @Test
    fun `buildPolygon returns null with fewer than three vertices`() {
        val state = MapDrawState()
        state.enterDraw()
        state.addVertex(Coordinate(0.0, 0.0))
        state.addVertex(Coordinate(1.0, 0.0))
        assertNull(state.buildPolygon())
    }

    @Test
    fun `buildPolygon closes the ring when three vertices placed`() {
        val state = MapDrawState()
        state.enterDraw()
        state.addVertex(Coordinate(0.0, 0.0))
        state.addVertex(Coordinate(2.0, 0.0))
        state.addVertex(Coordinate(2.0, 1.0))
        val polygon = state.buildPolygon()
        assertNotNull(polygon)
        assertTrue(polygon is Geometry.Polygon)
        val ring = (polygon as Geometry.Polygon).ring
        assertEquals(4, ring.size)
        assertEquals(ring.first(), ring.last())
    }

    @Test
    fun `restored state preserves mode and vertices`() {
        val state = MapDrawState(initialMode = MapEditMode.DRAW, initialVertices = listOf(Coordinate(1.0, 1.0)))
        assertEquals(MapEditMode.DRAW, state.mode)
        assertEquals(listOf(Coordinate(1.0, 1.0)), state.vertices)
    }
}
