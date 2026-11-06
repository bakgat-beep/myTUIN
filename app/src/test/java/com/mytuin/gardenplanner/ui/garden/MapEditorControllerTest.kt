package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MapEditorControllerTest {
    private fun polygon() =
        Geometry.Polygon(
            listOf(
                Coordinate(0.0, 0.0),
                Coordinate(4.0, 0.0),
                Coordinate(4.0, 4.0),
                Coordinate(0.0, 4.0),
                Coordinate(0.0, 0.0),
            ),
        )

    @Test
    fun `shape drag returns translated geometry on endDrag`() {
        val controller = MapEditorController()
        controller.beginShapeDrag(Coordinate(0.0, 0.0), polygon())
        controller.updateDrag(Coordinate(2.0, 3.0))
        val result = controller.endDrag()
        assertTrue(result is Geometry.Polygon)
        assertEquals(Coordinate(2.0, 3.0), (result as Geometry.Polygon).ring.first())
        assertFalse(controller.isDragging)
    }

    @Test
    fun `vertex drag returns polygon with only that vertex moved`() {
        val controller = MapEditorController()
        controller.beginVertexDrag(index = 2, startMeters = Coordinate(0.0, 0.0), originalGeometry = polygon())
        controller.updateDrag(Coordinate(1.0, 1.0))
        val result = controller.endDrag() as Geometry.Polygon
        assertEquals(Coordinate(5.0, 5.0), result.ring[2])
        assertEquals(Coordinate(0.0, 0.0), result.ring[0])
    }

    @Test
    fun `inProgressGeometry reflects current drag delta`() {
        val controller = MapEditorController()
        controller.beginShapeDrag(Coordinate(0.0, 0.0), polygon())
        controller.updateDrag(Coordinate(1.0, 0.0))
        val preview = controller.inProgressGeometry() as Geometry.Polygon
        assertEquals(Coordinate(1.0, 0.0), preview.ring.first())
    }

    @Test
    fun `inProgressGeometry is null when not dragging`() {
        assertNull(MapEditorController().inProgressGeometry())
    }

    @Test
    fun `cancel clears drag state`() {
        val controller = MapEditorController()
        controller.beginShapeDrag(Coordinate(0.0, 0.0), polygon())
        controller.cancel()
        assertFalse(controller.isDragging)
        assertNull(controller.inProgressGeometry())
    }

    @Test
    fun `endDrag is idempotent when nothing is dragging`() {
        assertNull(MapEditorController().endDrag())
    }
}
