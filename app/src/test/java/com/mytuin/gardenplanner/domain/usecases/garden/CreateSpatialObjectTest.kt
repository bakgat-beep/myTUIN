package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.NewSpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateSpatialObject.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateSpatialObjectTest {
    private val repository = FakeSpatialObjectRepository()
    private val idGenerator =
        FakeIdGenerator(nextSpatialObjectIdValue = "spatialobject_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createSpatialObject =
        CreateSpatialObject(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createSpatialObject(minimalInput())
            assertEquals("spatialobject_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createSpatialObject(minimalInput())

            val objects = repository.snapshot()
            assertEquals(1, objects.size)
            val spatialObject = objects.first()

            assertEquals("spatialobject_test_0001", spatialObject.id)
            assertEquals("garden_test_0001", spatialObject.gardenId)
            assertEquals(InfrastructureType.PATH, spatialObject.objectType)
            assertEquals(RecordStatus.ACTIVE, spatialObject.status)
            assertEquals(1_700_000_000_000L, spatialObject.createdAt)
            assertEquals(1_700_000_000_000L, spatialObject.updatedAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createSpatialObject(minimalInput())
            val spatialObject = repository.snapshot().first()

            assertNull(spatialObject.name)
            assertNull(spatialObject.description)
            assertNull(spatialObject.areaId)
            assertNull(spatialObject.notes)
        }

    @Test
    fun invoke_preserves_name_description_notes_and_area_id() =
        runBlocking {
            createSpatialObject(
                minimalInput().copy(
                    name = "North path",
                    description = "Maintenance access",
                    areaId = "area_test_0001",
                    notes = "Slopes gently",
                ),
            )
            val spatialObject = repository.snapshot().first()

            assertEquals("North path", spatialObject.name)
            assertEquals("Maintenance access", spatialObject.description)
            assertEquals("area_test_0001", spatialObject.areaId)
            assertEquals("Slopes gently", spatialObject.notes)
        }

    @Test
    fun invoke_preserves_point_geometry() =
        runBlocking {
            val point = Geometry.Point(Coordinate(2.0, 3.0))
            createSpatialObject(minimalInput().copy(geometry = point))

            assertEquals(point, repository.snapshot().first().geometry)
        }

    @Test
    fun invoke_preserves_line_geometry() =
        runBlocking {
            val line =
                Geometry.LineString(
                    listOf(Coordinate(0.0, 0.0), Coordinate(5.0, 0.0)),
                )
            createSpatialObject(minimalInput().copy(geometry = line))

            assertEquals(line, repository.snapshot().first().geometry)
        }

    @Test
    fun invoke_accepts_all_infrastructure_types() =
        runBlocking {
            InfrastructureType.entries.forEach { type ->
                createSpatialObject(minimalInput().copy(objectType = type))
            }
            assertEquals(18, repository.snapshot().size)
            assertEquals(
                InfrastructureType.entries.toSet(),
                repository.snapshot().map { it.objectType }.toSet(),
            )
        }

    private fun minimalInput(): NewSpatialObject =
        NewSpatialObject(
            gardenId = "garden_test_0001",
            objectType = InfrastructureType.PATH,
            geometry = Geometry.Point(Coordinate(0.0, 0.0)),
        )
}
