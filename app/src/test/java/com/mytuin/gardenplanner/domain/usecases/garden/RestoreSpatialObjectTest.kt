package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreSpatialObjectTest {
    private val repository = FakeSpatialObjectRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreSpatialObject(repository, clock)

    private val spatialObjectId = "spatialobject_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                SpatialObject(
                    id = spatialObjectId,
                    gardenId = "garden_test_0001",
                    objectType = InfrastructureType.PATH,
                    geometry = Geometry.Point(Coordinate(0.0, 0.0)),
                    status = RecordStatus.ARCHIVED,
                    name = "North path",
                    description = null,
                    areaId = null,
                    notes = null,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(spatialObjectId)
            assertEquals(
                RecordStatus.ACTIVE,
                repository.snapshot().single().status,
            )
        }

    @Test
    fun invoke_supplies_restoredAt_from_clock() =
        runBlocking {
            useCase(spatialObjectId)
            assertEquals(1_700_001_000_000L, repository.snapshot().single().updatedAt)
        }
}
