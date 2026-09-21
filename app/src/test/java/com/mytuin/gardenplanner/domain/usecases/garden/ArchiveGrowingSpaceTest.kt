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

class ArchiveGrowingSpaceTest {
    private val repository = FakeGrowingSpaceRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = ArchiveGrowingSpace(repository, clock)

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
                    status = RecordStatus.ACTIVE,
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
                    updatedAt = 1_700_000_000_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_archived() =
        runBlocking {
            useCase(spaceId)
            assertEquals(RecordStatus.ARCHIVED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_archivedAt_from_clock() =
        runBlocking {
            useCase(spaceId)
            assertEquals(1_700_000_500_000L, repository.snapshot().single().updatedAt)
        }
}
