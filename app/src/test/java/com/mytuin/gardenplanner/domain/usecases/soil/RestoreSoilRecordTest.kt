package com.mytuin.gardenplanner.domain.usecases.soil

import com.mytuin.gardenplanner.domain.model.garden.SoilRecord
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeSoilRecordRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreSoilRecordTest {
    private val repository = FakeSoilRecordRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreSoilRecord(repository, clock)

    private val soilRecordId = "soilrecord_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                SoilRecord(
                    id = soilRecordId,
                    gardenId = "garden_test_0001",
                    status = RecordStatus.ARCHIVED,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                    areaId = null,
                    growingSpaceId = null,
                    name = "Bed soil",
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_sets_status_to_active() =
        runBlocking {
            useCase(soilRecordId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_restoredAt_from_clock() =
        runBlocking {
            useCase(soilRecordId)
            assertEquals(1_700_001_000_000L, repository.snapshot().single().updatedAt)
        }
}
