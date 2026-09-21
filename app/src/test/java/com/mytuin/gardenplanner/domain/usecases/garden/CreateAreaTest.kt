package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.NewArea
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeAreaRepository
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateArea.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateAreaTest {
    private val repository = FakeAreaRepository()
    private val idGenerator =
        FakeIdGenerator(nextAreaIdValue = "area_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createArea =
        CreateArea(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createArea(minimalInput())
            assertEquals("area_test_0001", id)
        }

    @Test
    fun invoke_persists_an_area_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createArea(minimalInput())

            val areas = repository.snapshot()
            assertEquals(1, areas.size)
            val area = areas.first()

            assertEquals("area_test_0001", area.id)
            assertEquals("garden_test_0001", area.gardenId)
            assertEquals("Vegetable Garden", area.name)
            assertEquals(AreaType.ZONE, area.areaType)
            assertEquals(RecordStatus.ACTIVE, area.status)
            assertEquals(1_700_000_000_000L, area.createdAt)
            assertEquals(1_700_000_000_000L, area.updatedAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createArea(minimalInput())
            val area = repository.snapshot().first()

            assertNull(area.description)
            assertNull(area.geometry)
        }

    @Test
    fun invoke_preserves_description() =
        runBlocking {
            createArea(minimalInput().copy(description = "North side, sunny"))
            assertEquals("North side, sunny", repository.snapshot().first().description)
        }

    @Test
    fun invoke_preserves_polygon_geometry() =
        runBlocking {
            val polygon =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(5.0, 0.0),
                        Coordinate(5.0, 4.0),
                        Coordinate(0.0, 4.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            createArea(minimalInput().copy(geometry = polygon))

            assertEquals(polygon, repository.snapshot().first().geometry)
        }

    @Test
    fun invoke_accepts_all_three_area_types() =
        runBlocking {
            AreaType.entries.forEach { type ->
                createArea(minimalInput().copy(areaType = type))
            }
            assertEquals(3, repository.snapshot().size)
            assertEquals(
                AreaType.entries.toSet(),
                repository.snapshot().map { it.areaType }.toSet(),
            )
        }

    private fun minimalInput(): NewArea =
        NewArea(
            gardenId = "garden_test_0001",
            name = "Vegetable Garden",
            areaType = AreaType.ZONE,
        )
}
