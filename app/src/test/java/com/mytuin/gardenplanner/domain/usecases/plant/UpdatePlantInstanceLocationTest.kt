package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstanceLocation
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakePlantInstanceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Use case tests for UpdatePlantInstanceLocation.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class UpdatePlantInstanceLocationTest {
    private val repository = FakePlantInstanceRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)

    private val useCase =
        UpdatePlantInstanceLocation(
            repository = repository,
            clock = clock,
        )

    private val instanceId = "plantinstance_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                PlantInstance(
                    id = instanceId,
                    gardenId = "garden_test_0001",
                    plantId = "plant_test_0001",
                    status = RecordStatus.ACTIVE,
                    cultivarId = null,
                    growingSpaceId = "growingspace_test_0001",
                    spatialObjectId = null,
                    name = "Tomato in Bed 2",
                    quantity = null,
                    plannedDate = null,
                    plantedDate = null,
                    expectedEndDate = null,
                    removedDate = null,
                    plantingStock = null,
                    lifecycle = null,
                    geometry = null,
                    notes = null,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                ),
            )
        }

    @Test
    fun invoke_forwards_location_to_repository() =
        runBlocking {
            val location =
                NewPlantInstanceLocation(
                    growingSpaceId = "growingspace_test_0002",
                    spatialObjectId = "spatialobject_test_0001",
                    geometry = Geometry.Point(Coordinate(1.0, 2.0)),
                )

            useCase(instanceId, location)

            val call = repository.locationChangeCalls().single()
            assertEquals(instanceId, call.id)
            assertEquals(location, call.location)
        }

    @Test
    fun invoke_supplies_effectiveAt_from_clock() =
        runBlocking {
            useCase(instanceId, NewPlantInstanceLocation())

            assertEquals(1_700_000_500_000L, repository.locationChangeCalls().single().effectiveAt)
        }

    @Test
    fun invoke_passes_reason_through() =
        runBlocking {
            useCase(instanceId, NewPlantInstanceLocation(), reason = "Moved for rotation")

            assertEquals("Moved for rotation", repository.locationChangeCalls().single().reason)
        }

    @Test
    fun invoke_defaults_reason_to_null() =
        runBlocking {
            useCase(instanceId, NewPlantInstanceLocation())

            assertNull(repository.locationChangeCalls().single().reason)
        }

    @Test
    fun invoke_updates_current_location_on_the_stored_instance() =
        runBlocking {
            useCase(
                instanceId,
                NewPlantInstanceLocation(growingSpaceId = "growingspace_test_0002"),
            )

            val instance = repository.snapshot().first()
            assertEquals("growingspace_test_0002", instance.growingSpaceId)
            assertEquals(1_700_000_500_000L, instance.updatedAt)
        }

    @Test
    fun invoke_with_all_nulls_clears_the_location() =
        runBlocking {
            useCase(instanceId, NewPlantInstanceLocation())

            val instance = repository.snapshot().first()
            assertNull(instance.growingSpaceId)
            assertNull(instance.spatialObjectId)
            assertNull(instance.geometry)
        }
}
