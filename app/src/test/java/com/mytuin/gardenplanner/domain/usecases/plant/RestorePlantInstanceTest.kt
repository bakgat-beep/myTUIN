package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakePlantInstanceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestorePlantInstanceTest {
    private val repository = FakePlantInstanceRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestorePlantInstance(repository, clock)

    private val instanceId = "plantinstance_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                PlantInstance(
                    id = instanceId,
                    gardenId = "garden_test_0001",
                    plantId = "plant_test_0001",
                    status = RecordStatus.ARCHIVED,
                    cultivarId = null,
                    growingSpaceId = null,
                    spatialObjectId = null,
                    name = "Tomato in Bed 2",
                    quantity = null,
                    plannedDate = null,
                    plantedDate = null,
                    expectedEndDate = null,
                    removedDate = null,
                    plantingStock = null,
                    lifecycle = PlantInstanceLifecycle.HARVESTED,
                    geometry = null,
                    notes = null,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(instanceId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_restoredAt_from_clock() =
        runBlocking {
            useCase(instanceId)
            assertEquals(1_700_001_000_000L, repository.snapshot().single().updatedAt)
        }

    @Test
    fun invoke_does_not_change_lifecycle() =
        runBlocking {
            useCase(instanceId)
            assertEquals(
                PlantInstanceLifecycle.HARVESTED,
                repository.snapshot().single().lifecycle,
            )
        }
}
