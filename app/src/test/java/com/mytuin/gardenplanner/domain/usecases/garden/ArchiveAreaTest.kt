package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeAreaRepository
import com.mytuin.gardenplanner.testdoubles.FakeClock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArchiveAreaTest {
    private val repository = FakeAreaRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = ArchiveArea(repository, clock)

    private val areaId = "area_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Area(
                    id = areaId,
                    gardenId = "garden_test_0001",
                    name = "Vegetable Garden",
                    areaType = AreaType.ZONE,
                    description = null,
                    geometry = null,
                    status = RecordStatus.ACTIVE,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_archived() =
        runBlocking {
            useCase(areaId)
            assertEquals(RecordStatus.ARCHIVED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_archivedAt_from_clock() =
        runBlocking {
            useCase(areaId)
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
            useCase(areaId)
            val area = repository.snapshot().single()
            assertEquals(areaId, area.id)
            assertEquals("Vegetable Garden", area.name)
            assertEquals(1_700_000_000_000L, area.createdAt)
        }
}
