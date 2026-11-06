package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MapEditorGeometryOpsTest {
    private val delta = Coordinate(2.0, 3.0)

    private fun samplePolygon() =
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
    fun `translate point shifts both coordinates`() {
        val result = MapEditorGeometryOps.translate(Geometry.Point(Coordinate(1.0, 1.0)), delta) as Geometry.Point
        assertEquals(Coordinate(3.0, 4.0), result.at)
    }

    @Test
    fun `translate linestring shifts every vertex`() {
        val geometry =
            Geometry.LineString(listOf(Coordinate(0.0, 0.0), Coordinate(1.0, 1.0)))
        val result = MapEditorGeometryOps.translate(geometry, delta) as Geometry.LineString
        assertEquals(listOf(Coordinate(2.0, 3.0), Coordinate(3.0, 4.0)), result.vertices)
    }

    @Test
    fun `translate polygon preserves closed ring`() {
        val result = MapEditorGeometryOps.translate(samplePolygon(), delta) as Geometry.Polygon
        assertEquals(result.ring.first(), result.ring.last())
        assertEquals(Coordinate(2.0, 3.0), result.ring.first())
    }

    @Test
    fun `replaceVertex mid-ring replaces only that vertex`() {
        val result = MapEditorGeometryOps.replaceVertex(samplePolygon(), 1, delta)
        assertEquals(Coordinate(6.0, 3.0), result?.ring?.get(1))
        assertEquals(Coordinate(0.0, 0.0), result?.ring?.get(0))
    }

    @Test
    fun `replaceVertex at index 0 also updates closing coordinate`() {
        val result = MapEditorGeometryOps.replaceVertex(samplePolygon(), 0, delta)
        assertEquals(Coordinate(2.0, 3.0), result?.ring?.get(0))
        assertEquals(Coordinate(2.0, 3.0), result?.ring?.last())
    }

    @Test
    fun `replaceVertex at last index also updates opening coordinate`() {
        val result = MapEditorGeometryOps.replaceVertex(samplePolygon(), 4, delta)
        assertEquals(Coordinate(2.0, 3.0), result?.ring?.get(4))
        assertEquals(Coordinate(2.0, 3.0), result?.ring?.get(0))
    }

    @Test
    fun `replaceVertex returns null for null polygon`() {
        assertNull(MapEditorGeometryOps.replaceVertex(null, 0, delta))
    }

    @Test
    fun `replaceVertex returns null for out-of-bounds index`() {
        assertNull(MapEditorGeometryOps.replaceVertex(samplePolygon(), 99, delta))
    }

    @Test
    fun `deleteVertex removes an interior vertex`() {
        val result = MapEditorGeometryOps.deleteVertex(samplePolygon(), 1)
        assertNotNull(result)
        assertEquals(4, result!!.ring.size)
        assertEquals(Coordinate(0.0, 0.0), result.ring[0])
        assertEquals(Coordinate(4.0, 4.0), result.ring[1])
    }

    @Test
    fun `deleteVertex at endpoints returns null`() {
        assertNull(MapEditorGeometryOps.deleteVertex(samplePolygon(), 0))
        assertNull(MapEditorGeometryOps.deleteVertex(samplePolygon(), 4))
    }

    @Test
    fun `deleteVertex returns null when ring would become too small`() {
        val minimal =
            Geometry.Polygon(
                listOf(
                    Coordinate(0.0, 0.0),
                    Coordinate(1.0, 0.0),
                    Coordinate(1.0, 1.0),
                    Coordinate(0.0, 0.0),
                ),
            )
        assertNull(MapEditorGeometryOps.deleteVertex(minimal, 1))
    }

    @Test
    fun `insertVertexAtEdgeMidpoint places midpoint of chosen edge`() {
        val result = MapEditorGeometryOps.insertVertexAtEdgeMidpoint(samplePolygon(), 0)
        assertNotNull(result)
        assertEquals(6, result!!.ring.size)
        assertEquals(Coordinate(2.0, 0.0), result.ring[1])
    }

    @Test
    fun `insertVertexAtEdgeMidpoint returns null for out-of-range index`() {
        assertNull(MapEditorGeometryOps.insertVertexAtEdgeMidpoint(samplePolygon(), 99))
        assertNull(MapEditorGeometryOps.insertVertexAtEdgeMidpoint(samplePolygon(), -1))
    }
}
