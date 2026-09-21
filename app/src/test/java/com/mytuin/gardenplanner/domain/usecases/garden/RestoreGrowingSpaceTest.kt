package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreGrowingSpaceTest {
    private val repository = FakeGrowingSpaceRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreGrowingSpace(repository, clock)

    private val spaceId = "growingspace_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                GrowingSpace(
                    id = spaceId,
                    gardenId = "garden_test_0001",
                    name = "Bed 2",
                    spaceType = GrowingSpaceType.RAISED_BED,
                    status = RecordStatus.ARCHIVED,
                    geometry = null,
                    lengthMetres = null,
                    widthMetres = null,
                    heightMetres = null,
                    diameterMetres = null,
                    areaSquareMetres = null,
                    volumeCubicMetres = null,
                    description = null,
                    notes = null,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(spaceId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }
}
