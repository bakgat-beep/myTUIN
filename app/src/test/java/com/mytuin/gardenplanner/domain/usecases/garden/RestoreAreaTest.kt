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

class RestoreAreaTest {
    private val repository = FakeAreaRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreArea(repository, clock)

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
                    status = RecordStatus.ARCHIVED,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(areaId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_restoredAt_from_clock() =
        runBlocking {
            useCase(areaId)
            assertEquals(1_700_001_000_000L, repository.snapshot().single().updatedAt)
        }

    @Test
    fun restore_promotes_a_formerly_null_status_area_to_active() =
        runBlocking {
            // Documented limitation, matching RestoreGarden (A240): a
            // null pre-archive status is not preserved. This test
            // asserts the actual behaviour.
            repository.insert(
                Area(
                    id = "area_test_0002",
                    gardenId = "garden_test_0001",
                    name = "Null status",
                    areaType = AreaType.SECTION,
                    description = null,
                    geometry = null,
                    status = null,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                ),
            )
            // Manually archive the null-status area to simulate the
            // pre-restore state.
            repository.archive("area_test_0002", 1_700_000_500_000L)
            useCase("area_test_0002")

            assertEquals(
                RecordStatus.ACTIVE,
                repository.snapshot().first { it.id == "area_test_0002" }.status,
            )
        }
}
