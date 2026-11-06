package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MapFilterStateTest {
    private fun areaShape() = MapShape("a1", MapShapeKind.Area, Geometry.Point(Coordinate(0.0, 0.0)))

    private fun gsShape(type: GrowingSpaceType = GrowingSpaceType.RAISED_BED) =
        MapShape("gs1", MapShapeKind.GrowingSpace(type), Geometry.Point(Coordinate(0.0, 0.0)))

    private fun soShape(type: InfrastructureType = InfrastructureType.FENCE) =
        MapShape("so1", MapShapeKind.SpatialObject(type), Geometry.Point(Coordinate(0.0, 0.0)))

    @Test
    fun `default state shows every shape`() {
        val state = MapFilterState()
        assertTrue(state.isShapeVisible(areaShape()))
        assertTrue(state.isShapeVisible(gsShape()))
        assertTrue(state.isShapeVisible(soShape()))
        assertTrue(state.isDefault)
    }

    @Test
    fun `hiding areas makes only areas invisible`() {
        val state = MapFilterState(showAreas = false)
        assertFalse(state.isShapeVisible(areaShape()))
        assertTrue(state.isShapeVisible(gsShape()))
        assertTrue(state.isShapeVisible(soShape()))
        assertFalse(state.isDefault)
    }

    @Test
    fun `hiding growing spaces kind hides all its shapes`() {
        val state = MapFilterState(showGrowingSpaces = false)
        assertFalse(state.isShapeVisible(gsShape(GrowingSpaceType.RAISED_BED)))
        assertFalse(state.isShapeVisible(gsShape(GrowingSpaceType.POT)))
        assertTrue(state.isShapeVisible(soShape()))
    }

    @Test
    fun `hiding a specific growing space type hides only that type`() {
        val state = MapFilterState(hiddenGrowingSpaceTypes = setOf(GrowingSpaceType.POT))
        assertTrue(state.isShapeVisible(gsShape(GrowingSpaceType.RAISED_BED)))
        assertFalse(state.isShapeVisible(gsShape(GrowingSpaceType.POT)))
    }

    @Test
    fun `hiding a specific infrastructure type hides only that type`() {
        val state = MapFilterState(hiddenInfrastructureTypes = setOf(InfrastructureType.FENCE))
        assertTrue(state.isShapeVisible(soShape(InfrastructureType.PATH)))
        assertFalse(state.isShapeVisible(soShape(InfrastructureType.FENCE)))
    }

    @Test
    fun `visibleKindCount reflects layer switches`() {
        assertEquals(3, MapFilterState().visibleKindCount)
        assertEquals(2, MapFilterState(showAreas = false).visibleKindCount)
        assertEquals(
            0,
            MapFilterState(
                showAreas = false,
                showGrowingSpaces = false,
                showSpatialObjects = false,
            ).visibleKindCount,
        )
    }

    @Test
    fun `isDefault only true when no filters applied`() {
        assertTrue(MapFilterState().isDefault)
        assertFalse(MapFilterState(showAreas = false).isDefault)
        assertFalse(
            MapFilterState(hiddenGrowingSpaceTypes = setOf(GrowingSpaceType.POT)).isDefault,
        )
        assertFalse(
            MapFilterState(hiddenInfrastructureTypes = setOf(InfrastructureType.FENCE)).isDefault,
        )
    }
}
