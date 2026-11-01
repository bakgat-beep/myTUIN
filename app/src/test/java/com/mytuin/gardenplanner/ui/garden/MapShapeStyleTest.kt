package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class MapShapeStyleTest {
    @Test
    fun `every growing space type has a style`() {
        GrowingSpaceType.entries.forEach { type ->
            val style = MapShapeStyle.forKind(MapShapeKind.GrowingSpace(type))
            assertNotNull(style)
        }
    }

    @Test
    fun `every infrastructure type has a style`() {
        InfrastructureType.entries.forEach { type ->
            val style = MapShapeStyle.forKind(MapShapeKind.SpatialObject(type))
            assertNotNull(style)
        }
    }

    @Test
    fun `area style is separate from any growing space`() {
        val areaStyle = MapShapeStyle.forKind(MapShapeKind.Area)
        GrowingSpaceType.entries.forEach { type ->
            val spaceStyle = MapShapeStyle.forKind(MapShapeKind.GrowingSpace(type))
            assertNotEquals(areaStyle, spaceStyle)
        }
    }

    @Test
    fun `fence and wall have full transparent fill because they are line-only`() {
        val fence = MapShapeStyle.forKind(MapShapeKind.SpatialObject(InfrastructureType.FENCE))
        val wall = MapShapeStyle.forKind(MapShapeKind.SpatialObject(InfrastructureType.WALL))
        assertEquals(0.0f, fence.fillOpacity)
        assertEquals(0.0f, wall.fillOpacity)
    }
}
