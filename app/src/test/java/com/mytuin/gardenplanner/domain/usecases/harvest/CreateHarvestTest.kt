package com.mytuin.gardenplanner.domain.usecases.harvest

import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewHarvest
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestSizeCategory
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeHarvestRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateHarvest.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateHarvestTest {
    private val repository = FakeHarvestRepository()
    private val idGenerator =
        FakeIdGenerator(
            nextHarvestIdValue = "harvest_test_0001",
            nextActivityIdValue = "activity_test_0001",
        )
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createHarvest =
        CreateHarvest(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_harvest_id() =
        runBlocking {
            val id = createHarvest(minimalInput())
            assertEquals("harvest_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_ids_and_createdAt() =
        runBlocking {
            createHarvest(minimalInput())

            val harvests = repository.snapshot()
            assertEquals(1, harvests.size)
            val harvest = harvests.first()

            assertEquals("harvest_test_0001", harvest.id)
            assertEquals("activity_test_0001", harvest.activityId)
            assertEquals("garden_test_0001", harvest.gardenId)
            assertEquals(1_700_000_000_000L, harvest.createdAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createHarvest(minimalInput())
            val harvest = repository.snapshot().first()

            assertNull(harvest.sizeCategory)
            assertNull(harvest.plantInstanceId)
            assertNull(harvest.growingSpaceId)
            assertNull(harvest.notes)
        }

    @Test
    fun invoke_preserves_quantity_and_unit() =
        runBlocking {
            createHarvest(
                minimalInput().copy(
                    quantity = 12.0,
                    unit = ActivityQuantityUnit.COUNT,
                ),
            )
            val harvest = repository.snapshot().first()

            assertEquals(12.0, harvest.quantity, 0.0)
            assertEquals(ActivityQuantityUnit.COUNT, harvest.unit)
        }

    @Test
    fun invoke_preserves_plant_instance_and_growing_space_references() =
        runBlocking {
            createHarvest(
                minimalInput().copy(
                    plantInstanceId = "plantinstance_test_0001",
                    growingSpaceId = "growingspace_test_0001",
                ),
            )
            val harvest = repository.snapshot().first()

            assertEquals("plantinstance_test_0001", harvest.plantInstanceId)
            assertEquals("growingspace_test_0001", harvest.growingSpaceId)
        }

    @Test
    fun invoke_preserves_size_category_and_notes() =
        runBlocking {
            createHarvest(
                minimalInput().copy(
                    sizeCategory = HarvestSizeCategory.LARGE,
                    notes = "First pick of the season",
                ),
            )
            val harvest = repository.snapshot().first()

            assertEquals(HarvestSizeCategory.LARGE, harvest.sizeCategory)
            assertEquals("First pick of the season", harvest.notes)
        }

    @Test
    fun invoke_preserves_date_only_known_date() =
        runBlocking {
            createHarvest(
                minimalInput().copy(
                    date = KnownDate(LocalDate.of(2026, 9, 15)),
                ),
            )
            val harvest = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), harvest.date.date)
            assertNull(harvest.date.time)
        }

    @Test
    fun invoke_preserves_timestamp_known_date() =
        runBlocking {
            createHarvest(
                minimalInput().copy(
                    date =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(9, 30),
                        ),
                ),
            )
            val harvest = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), harvest.date.date)
            assertEquals(LocalTime.of(9, 30), harvest.date.time)
        }

    @Test
    fun invoke_accepts_all_five_size_categories() =
        runBlocking {
            HarvestSizeCategory.entries.forEach { category ->
                createHarvest(minimalInput().copy(sizeCategory = category))
            }
            assertEquals(5, repository.snapshot().size)
            assertEquals(
                HarvestSizeCategory.entries.toSet(),
                repository.snapshot().mapNotNull { it.sizeCategory }.toSet(),
            )
        }

    private fun minimalInput(): NewHarvest =
        NewHarvest(
            gardenId = "garden_test_0001",
            date = KnownDate(LocalDate.of(2026, 9, 15)),
            quantity = 5.0,
            unit = ActivityQuantityUnit.COUNT,
        )
}
