package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstance
import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakePlantInstanceRepository
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreatePlantInstance.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreatePlantInstanceTest {
    private val repository = FakePlantInstanceRepository()
    private val idGenerator =
        FakeIdGenerator(nextPlantInstanceIdValue = "plantinstance_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createPlantInstance =
        CreatePlantInstance(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createPlantInstance(minimalInput())
            assertEquals("plantinstance_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createPlantInstance(minimalInput())

            val instances = repository.snapshot()
            assertEquals(1, instances.size)
            val instance = instances.first()

            assertEquals("plantinstance_test_0001", instance.id)
            assertEquals("garden_test_0001", instance.gardenId)
            assertEquals("plant_test_0001", instance.plantId)
            assertEquals(RecordStatus.ACTIVE, instance.status)
            assertEquals(1_700_000_000_000L, instance.createdAt)
            assertEquals(1_700_000_000_000L, instance.updatedAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createPlantInstance(minimalInput())
            val instance = repository.snapshot().first()

            assertNull(instance.cultivarId)
            assertNull(instance.growingSpaceId)
            assertNull(instance.spatialObjectId)
            assertNull(instance.name)
            assertNull(instance.quantity)
            assertNull(instance.plannedDate)
            assertNull(instance.plantedDate)
            assertNull(instance.expectedEndDate)
            assertNull(instance.removedDate)
            assertNull(instance.plantingStock)
            assertNull(instance.lifecycle)
            assertNull(instance.geometry)
            assertNull(instance.notes)
        }

    @Test
    fun invoke_preserves_name_quantity_and_parent_references() =
        runBlocking {
            createPlantInstance(
                minimalInput().copy(
                    cultivarId = "cultivar_test_0001",
                    growingSpaceId = "growingspace_test_0001",
                    spatialObjectId = "spatialobject_test_0001",
                    name = "Tomato in Bed 2",
                    quantity = 3,
                    notes = "Planted along the north edge",
                ),
            )
            val instance = repository.snapshot().first()

            assertEquals("cultivar_test_0001", instance.cultivarId)
            assertEquals("growingspace_test_0001", instance.growingSpaceId)
            assertEquals("spatialobject_test_0001", instance.spatialObjectId)
            assertEquals("Tomato in Bed 2", instance.name)
            assertEquals(3, instance.quantity)
            assertEquals("Planted along the north edge", instance.notes)
        }

    @Test
    fun invoke_preserves_date_only_known_date() =
        runBlocking {
            createPlantInstance(
                minimalInput().copy(
                    plannedDate = KnownDate(LocalDate.of(2026, 9, 15)),
                ),
            )
            val instance = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), instance.plannedDate?.date)
            assertNull(instance.plannedDate?.time)
        }

    @Test
    fun invoke_preserves_timestamp_known_date() =
        runBlocking {
            createPlantInstance(
                minimalInput().copy(
                    plantedDate =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(9, 30),
                        ),
                ),
            )
            val instance = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), instance.plantedDate?.date)
            assertEquals(LocalTime.of(9, 30), instance.plantedDate?.time)
        }

    @Test
    fun invoke_preserves_all_four_date_fields_independently() =
        runBlocking {
            createPlantInstance(
                minimalInput().copy(
                    plannedDate = KnownDate(LocalDate.of(2026, 3, 1)),
                    plantedDate = KnownDate(LocalDate.of(2026, 3, 15), LocalTime.of(9, 30)),
                    expectedEndDate = KnownDate(LocalDate.of(2026, 8, 1)),
                    removedDate = KnownDate(LocalDate.of(2026, 9, 1), LocalTime.of(17, 0)),
                ),
            )
            val instance = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 3, 1), instance.plannedDate?.date)
            assertEquals(LocalDate.of(2026, 3, 15), instance.plantedDate?.date)
            assertEquals(LocalTime.of(9, 30), instance.plantedDate?.time)
            assertEquals(LocalDate.of(2026, 8, 1), instance.expectedEndDate?.date)
            assertEquals(LocalDate.of(2026, 9, 1), instance.removedDate?.date)
            assertEquals(LocalTime.of(17, 0), instance.removedDate?.time)
        }

    @Test
    fun invoke_preserves_planting_stock_and_lifecycle() =
        runBlocking {
            createPlantInstance(
                minimalInput().copy(
                    plantingStock = PlantingStockType.SEEDLING,
                    lifecycle = PlantInstanceLifecycle.PLANTED,
                ),
            )
            val instance = repository.snapshot().first()

            assertEquals(PlantingStockType.SEEDLING, instance.plantingStock)
            assertEquals(PlantInstanceLifecycle.PLANTED, instance.lifecycle)
        }

    @Test
    fun invoke_preserves_point_geometry() =
        runBlocking {
            val point = Geometry.Point(Coordinate(3.0, 4.0))
            createPlantInstance(minimalInput().copy(geometry = point))

            assertEquals(point, repository.snapshot().first().geometry)
        }

    @Test
    fun invoke_accepts_all_eight_lifecycle_values() =
        runBlocking {
            PlantInstanceLifecycle.entries.forEach { lifecycle ->
                createPlantInstance(minimalInput().copy(lifecycle = lifecycle))
            }
            assertEquals(8, repository.snapshot().size)
            assertEquals(
                PlantInstanceLifecycle.entries.toSet(),
                repository.snapshot().mapNotNull { it.lifecycle }.toSet(),
            )
        }

    @Test
    fun invoke_accepts_all_twelve_planting_stock_values() =
        runBlocking {
            PlantingStockType.entries.forEach { stock ->
                createPlantInstance(minimalInput().copy(plantingStock = stock))
            }
            assertEquals(12, repository.snapshot().size)
            assertEquals(
                PlantingStockType.entries.toSet(),
                repository.snapshot().mapNotNull { it.plantingStock }.toSet(),
            )
        }

    private fun minimalInput(): NewPlantInstance =
        NewPlantInstance(
            gardenId = "garden_test_0001",
            plantId = "plant_test_0001",
        )
}
