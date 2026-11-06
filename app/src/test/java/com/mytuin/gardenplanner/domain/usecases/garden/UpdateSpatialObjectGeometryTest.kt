package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class UpdateSpatialObjectGeometryTest {
    private fun sampleObject() =
        SpatialObject(
            id = "spatialobject_1",
            gardenId = "garden_1",
            objectType = InfrastructureType.FENCE,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
            status = RecordStatus.ACTIVE,
            name = "Back fence",
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1L,
            updatedAt = 1L,
        )

    private fun newGeometry() =
        Geometry.Polygon(
            listOf(
                Coordinate(0.0, 0.0),
                Coordinate(4.0, 0.0),
                Coordinate(4.0, 1.0),
                Coordinate(0.0, 1.0),
                Coordinate(0.0, 0.0),
            ),
        )

    @Test
    fun `invoke delegates to repository with clock time and reason`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            val obj = sampleObject()
            repo.insert(obj)
            val clock = FakeClock()
            val useCase = UpdateSpatialObjectGeometry(repo, clock)
            val newGeom = newGeometry()

            useCase(obj.id, newGeom, "Edited on map")

            val calls = repo.geometryUpdateCalls()
            assertEquals(1, calls.size)
            assertEquals(obj.id, calls[0].id)
            assertEquals(newGeom, calls[0].geometry)
            assertEquals(clock.nowMillis(), calls[0].at)
            assertEquals("Edited on map", calls[0].reason)
            assertEquals(newGeom, repo.snapshot().first().geometry)
        }

    @Test
    fun `reason is optional and defaults to null`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            val obj = sampleObject()
            repo.insert(obj)
            val useCase = UpdateSpatialObjectGeometry(repo, FakeClock())

            useCase(obj.id, newGeometry())

            assertEquals(null, repo.geometryUpdateCalls().first().reason)
        }

    @Test
    fun `missing object throws NotFoundError and does not update`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            val useCase = UpdateSpatialObjectGeometry(repo, FakeClock())

            assertThrows(NotFoundError::class.java) {
                kotlinx.coroutines.runBlocking {
                    useCase("spatialobject_missing", newGeometry(), null)
                }
            }
            assertEquals(0, repo.geometryUpdateCalls().size)
        }
}
