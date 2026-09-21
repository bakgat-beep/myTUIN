package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreGardenTest {
    private val repository = FakeGardenRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreGarden(repository, clock)

    private val gardenId = "garden_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Garden(
                    id = gardenId,
                    name = "Back Garden",
                    description = null,
                    countryCode = null,
                    region = null,
                    locality = null,
                    latitude = null,
                    longitude = null,
                    timezone = null,
                    hemisphere = Hemisphere.UNKNOWN,
                    status = RecordStatus.ARCHIVED,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(gardenId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_restoredAt_from_clock() =
        runBlocking {
            useCase(gardenId)
            assertEquals(1_700_001_000_000L, repository.snapshot().single().updatedAt)
        }

    @Test
    fun restore_promotes_a_formerly_draft_garden_to_active() =
        runBlocking {
            // Documented limitation: the pre-archive status is not
            // preserved. This test asserts the actual behaviour.
            useCase(gardenId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }
}
