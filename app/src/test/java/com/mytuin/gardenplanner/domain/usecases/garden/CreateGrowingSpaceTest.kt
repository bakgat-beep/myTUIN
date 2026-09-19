package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.NewGrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateGrowingSpace.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateGrowingSpaceTest {
    private val repository = FakeGrowingSpaceRepository()
    private val idGenerator =
        FakeIdGenerator(
            nextGrowingSpaceIdValue = "growingspace_test_0001",
        )
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createGrowingSpace =
        CreateGrowingSpace(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createGrowingSpace(minimalInput())
            assertEquals("growingspace_test_0001", id)
        }

    @Test
    fun invoke_persists_a_space_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createGrowingSpace(minimalInput())

            val spaces = repository.snapshot()
            assertEquals(1, spaces.size)
            val space = spaces.first()

            assertEquals("growingspace_test_0001", space.id)
            assertEquals("garden_test_0001", space.gardenId)
            assertEquals("Bed 2", space.name)
            assertEquals(GrowingSpaceType.RAISED_BED, space.spaceType)
            assertEquals(RecordStatus.ACTIVE, space.status)
            assertEquals(1_700_000_000_000L, space.createdAt)
            assertEquals(1_700_000_000_000L, space.updatedAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createGrowingSpace(minimalInput())
            val space = repository.snapshot().first()

            assertNull(space.geometry)
            assertNull(space.lengthMetres)
            assertNull(space.widthMetres)
            assertNull(space.heightMetres)
            assertNull(space.diameterMetres)
            assertNull(space.areaSquareMetres)
            assertNull(space.volumeCubicMetres)
            assertNull(space.description)
            assertNull(space.notes)
        }

    @Test
    fun invoke_preserves_dimensions() =
        runBlocking {
            createGrowingSpace(
                minimalInput().copy(
                    lengthMetres = 3.0,
                    widthMetres = 1.2,
                    heightMetres = 0.4,
                    areaSquareMetres = 3.6,
                ),
            )
            val space = repository.snapshot().first()

            assertEquals(3.0, space.lengthMetres!!, 0.0)
            assertEquals(1.2, space.widthMetres!!, 0.0)
            assertEquals(0.4, space.heightMetres!!, 0.0)
            assertEquals(3.6, space.areaSquareMetres!!, 0.0)
        }

    @Test
    fun invoke_preserves_polygon_geometry() =
        runBlocking {
            val polygon =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            createGrowingSpace(minimalInput().copy(geometry = polygon))

            assertEquals(polygon, repository.snapshot().first().geometry)
        }

    private fun minimalInput(): NewGrowingSpace =
        NewGrowingSpace(
            gardenId = "garden_test_0001",
            name = "Bed 2",
            spaceType = GrowingSpaceType.RAISED_BED,
        )
}
