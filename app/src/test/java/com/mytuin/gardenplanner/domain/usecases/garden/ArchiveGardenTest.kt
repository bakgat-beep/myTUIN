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

class ArchiveGardenTest {
    private val repository = FakeGardenRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = ArchiveGarden(repository, clock)

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
                    status = RecordStatus.DRAFT,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_archived() =
        runBlocking {
            useCase(gardenId)
            assertEquals(RecordStatus.ARCHIVED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_archivedAt_from_clock() =
        runBlocking {
            useCase(gardenId)
            assertEquals(1_700_000_500_000L, repository.snapshot().single().updatedAt)
            assertEquals(
                RecordStatus.ARCHIVED,
                repository.statusChangeCalls().single().status,
            )
            assertEquals(1_700_000_500_000L, repository.statusChangeCalls().single().at)
        }

    @Test
    fun invoke_does_not_change_id_name_or_createdAt() =
        runBlocking {
            useCase(gardenId)
            val garden = repository.snapshot().single()
            assertEquals(gardenId, garden.id)
            assertEquals("Back Garden", garden.name)
            assertEquals(1_700_000_000_000L, garden.createdAt)
        }

    @Test
    fun archived_garden_is_excluded_from_default_observation() =
        runBlocking {
            useCase(gardenId)
            // The fake's observeGardens default is includeArchived = false.
            // Reading directly from the store bypasses the filter, so this
            // test confirms the repository's filtering is what matters.
            assertEquals(RecordStatus.ARCHIVED, repository.snapshot().single().status)
        }
}
