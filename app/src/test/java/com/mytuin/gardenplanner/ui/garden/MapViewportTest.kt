package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MapViewportTest {
    @Test
    fun `empty shapes returns fallback viewport`() {
        val viewport = MapViewport.union(emptyList())
        assertEquals(MapViewport.FALLBACK, viewport)
    }

    @Test
    fun `single point shape returns padded viewport around it`() {
        val shape =
            MapShape(
                id = "p1",
                kind = MapShapeKind.GrowingSpace(GrowingSpaceType.POT),
                geometry = Geometry.Point(Coordinate(0.0, 0.0)),
            )
        val viewport = MapViewport.union(listOf(shape))
        assertTrue(viewport.minX < 0.0)
        assertTrue(viewport.minY < 0.0)
        assertTrue(viewport.maxX > 0.0)
        assertTrue(viewport.maxY > 0.0)
        // Padding is 3 m on each side.
        assertEquals(-3.0, viewport.minX)
        assertEquals(-3.0, viewport.minY)
        assertEquals(3.0, viewport.maxX)
        assertEquals(3.0, viewport.maxY)
    }

    @Test
    fun `two shapes are unioned`() {
        val a =
            MapShape(
                id = "a",
                kind = MapShapeKind.GrowingSpace(GrowingSpaceType.RAISED_BED),
                geometry =
                    Geometry.Polygon(
                        listOf(
                            Coordinate(0.0, 0.0),
                            Coordinate(1.0, 0.0),
                            Coordinate(1.0, 1.0),
                            Coordinate(0.0, 1.0),
                            Coordinate(0.0, 0.0),
                        ),
                    ),
            )
        val b =
            MapShape(
                id = "b",
                kind = MapShapeKind.SpatialObject(com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType.PATH),
                geometry =
                    Geometry.LineString(
                        listOf(
                            Coordinate(5.0, 5.0),
                            Coordinate(10.0, 5.0),
                        ),
                    ),
            )
        val viewport = MapViewport.union(listOf(a, b))
        assertEquals(-3.0, viewport.minX)
        assertEquals(13.0, viewport.maxX)
        assertEquals(-3.0, viewport.minY)
        assertEquals(8.0, viewport.maxY)
    }

    @Test
    fun `largest dimension reflects wider extent`() {
        val viewport = MapViewport(minX = 0.0, minY = 0.0, maxX = 10.0, maxY = 4.0)
        assertEquals(10.0, viewport.largestDimensionMetres)
    }

    @Test
    fun `fallback has non-degenerate extent`() {
        val viewport = MapViewport.FALLBACK
        assertFalse(viewport.widthMetres == 0.0)
        assertFalse(viewport.heightMetres == 0.0)
    }
}
