package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GardenHistoryTimelineTest {
    private val now = 10_000L
    private val t0 = 1_000L
    private val t1 = 5_000L
    private val tMid = 3_000L

    private fun polygonX(x: Double) =
        Geometry.Polygon(
            listOf(
                Coordinate(x, 0.0),
                Coordinate(x + 1.0, 0.0),
                Coordinate(x + 1.0, 1.0),
                Coordinate(x, 1.0),
                Coordinate(x, 0.0),
            ),
        )

    private fun spaceSnapshot(
        id: String = "gs_1",
        createdAt: Long = t0,
        currentGeometry: Geometry? = polygonX(0.0),
        history: List<GrowingSpaceHistory> = emptyList(),
    ) = GrowingSpaceHistorySnapshot(
        id = id,
        type = GrowingSpaceType.RAISED_BED,
        createdAt = createdAt,
        currentGeometry = currentGeometry,
        history = history,
    )

    private fun spaceHistoryRow(
        id: String = "gs_1",
        geometry: Geometry?,
        validFrom: Long,
        validTo: Long,
    ) = GrowingSpaceHistory(
        id = "hist_$id",
        growingSpaceId = id,
        geometry = geometry,
        lengthMetres = null,
        widthMetres = null,
        heightMetres = null,
        diameterMetres = null,
        areaSquareMetres = null,
        volumeCubicMetres = null,
        validFrom = validFrom,
        validTo = validTo,
        recordedAt = validTo,
        reason = null,
    )

    private fun objectSnapshot(
        id: String = "so_1",
        createdAt: Long = t0,
        currentGeometry: Geometry = polygonX(0.0),
        history: List<SpatialObjectHistory> = emptyList(),
    ) = SpatialObjectHistorySnapshot(
        id = id,
        type = InfrastructureType.FENCE,
        createdAt = createdAt,
        currentGeometry = currentGeometry,
        history = history,
    )

    private fun objectHistoryRow(
        id: String = "so_1",
        geometry: Geometry,
        validFrom: Long,
        validTo: Long,
    ) = SpatialObjectHistory(
        id = "hist_$id",
        spatialObjectId = id,
        geometry = geometry,
        validFrom = validFrom,
        validTo = validTo,
        recordedAt = validTo,
        reason = null,
    )

    @Test
    fun `empty inputs return null`() {
        assertNull(GardenHistoryTimeline.build(now, emptyList(), emptyList()))
    }

    @Test
    fun `space without current geometry and no history returns null`() {
        val spaces = listOf(spaceSnapshot(currentGeometry = null))
        assertNull(GardenHistoryTimeline.build(now, spaces, emptyList()))
    }

    @Test
    fun `single space no history has hasAnyHistory false`() {
        val spaces = listOf(spaceSnapshot(currentGeometry = polygonX(0.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())
        assertNotNull(timeline)
        assertFalse(timeline!!.hasAnyHistory)
        assertEquals(t0, timeline.minTime)
        assertEquals(now, timeline.maxTime)
    }

    @Test
    fun `space before createdAt is not in shapes`() {
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = polygonX(0.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        val shapes = timeline.shapesAt(t0 - 1L)
        assertEquals(0, shapes.size)
    }

    @Test
    fun `space with no history falls back to current geometry at any T after createdAt`() {
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = polygonX(0.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        val shapes = timeline.shapesAt(tMid)
        assertEquals(1, shapes.size)
        assertEquals(polygonX(0.0), shapes.first().geometry)
    }

    @Test
    fun `space with one history row returns row geometry during its period`() {
        val oldGeom = polygonX(0.0)
        val newGeom = polygonX(5.0)
        val history = listOf(spaceHistoryRow(geometry = oldGeom, validFrom = t0, validTo = t1))
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = newGeom, history = history))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        assertEquals(oldGeom, timeline.shapesAt(tMid).first().geometry)
    }

    @Test
    fun `space with one history row falls back to current geometry after validTo`() {
        val oldGeom = polygonX(0.0)
        val newGeom = polygonX(5.0)
        val history = listOf(spaceHistoryRow(geometry = oldGeom, validFrom = t0, validTo = t1))
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = newGeom, history = history))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        assertEquals(newGeom, timeline.shapesAt(now).first().geometry)
    }

    @Test
    fun `multiple history rows chain correctly`() {
        val g1 = polygonX(0.0)
        val g2 = polygonX(5.0)
        val g3 = polygonX(10.0)
        val history =
            listOf(
                spaceHistoryRow(geometry = g1, validFrom = t0, validTo = 2_000L),
                spaceHistoryRow(geometry = g2, validFrom = 2_000L, validTo = 4_000L),
            )
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = g3, history = history))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        assertEquals(g1, timeline.shapesAt(1_500L).first().geometry)
        assertEquals(g2, timeline.shapesAt(3_000L).first().geometry)
        assertEquals(g3, timeline.shapesAt(now).first().geometry)
    }

    @Test
    fun `spatial object reconstruction mirrors growing space logic`() {
        val g1 = polygonX(0.0)
        val g2 = polygonX(9.0)
        val history = listOf(objectHistoryRow(geometry = g1, validFrom = t0, validTo = t1))
        val objects = listOf(objectSnapshot(createdAt = t0, currentGeometry = g2, history = history))
        val timeline = GardenHistoryTimeline.build(now, emptyList(), objects)!!
        assertEquals(g1, timeline.shapesAt(tMid).first().geometry)
        assertEquals(g2, timeline.shapesAt(now).first().geometry)
    }

    @Test
    fun `mixed spaces and objects produce combined shapes`() {
        val spaces = listOf(spaceSnapshot(id = "gs_1", createdAt = t0, currentGeometry = polygonX(0.0)))
        val objects = listOf(objectSnapshot(id = "so_1", createdAt = t0, currentGeometry = polygonX(9.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, objects)!!
        val shapes = timeline.shapesAt(tMid)
        assertEquals(2, shapes.size)
        assertTrue(shapes.any { it.id == "gs_1" })
        assertTrue(shapes.any { it.id == "so_1" })
    }

    @Test
    fun `minTime is earliest createdAt across entities`() {
        val spaces = listOf(spaceSnapshot(id = "gs_1", createdAt = 5_000L, currentGeometry = polygonX(0.0)))
        val objects = listOf(objectSnapshot(id = "so_1", createdAt = 1_000L, currentGeometry = polygonX(9.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, objects)!!
        assertEquals(1_000L, timeline.minTime)
    }

    @Test
    fun `time after maxTime falls through to current geometry`() {
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = polygonX(0.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        val shapes = timeline.shapesAt(now + 100_000L)
        assertEquals(1, shapes.size)
    }

    @Test
    fun `time before minTime returns no shapes`() {
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = polygonX(0.0)))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        val shapes = timeline.shapesAt(0L)
        assertEquals(0, shapes.size)
    }

    @Test
    fun `viewport includes geometry from every history row`() {
        val g1 = polygonX(0.0)
        val g2 = polygonX(100.0)
        val history = listOf(spaceHistoryRow(geometry = g1, validFrom = t0, validTo = t1))
        val spaces = listOf(spaceSnapshot(createdAt = t0, currentGeometry = g2, history = history))
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        assertTrue(timeline.viewport.maxX >= 101.0)
    }

    @Test
    fun `hasAnyHistory is true when any entity has any row`() {
        val spaces =
            listOf(
                spaceSnapshot(
                    id = "gs_1",
                    currentGeometry = polygonX(0.0),
                    history = listOf(spaceHistoryRow(geometry = polygonX(5.0), validFrom = t0, validTo = t1)),
                ),
            )
        val timeline = GardenHistoryTimeline.build(now, spaces, emptyList())!!
        assertTrue(timeline.hasAnyHistory)
    }
}
