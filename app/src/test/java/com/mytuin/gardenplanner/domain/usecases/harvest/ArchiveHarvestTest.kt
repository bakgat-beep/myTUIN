package com.mytuin.gardenplanner.domain.usecases.harvest

import com.mytuin.gardenplanner.domain.model.garden.Harvest
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeHarvestRepository
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArchiveHarvestTest {
    private val repository = FakeHarvestRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = ArchiveHarvest(repository, clock)

    private val harvestId = "harvest_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Harvest(
                    id = harvestId,
                    activityId = "activity_test_0001",
                    gardenId = "garden_test_0001",
                    date = KnownDate(LocalDate.of(2026, 9, 15)),
                    quantity = 5.0,
                    unit = ActivityQuantityUnit.COUNT,
                    createdAt = 1_700_000_000_000L,
                    sizeCategory = null,
                    plantInstanceId = null,
                    growingSpaceId = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_calls_archive_on_the_repository() =
        runBlocking {
            useCase(harvestId)

            val call = repository.archiveCalls().single()
            assertEquals(harvestId, call.id)
            assertEquals(1_700_000_500_000L, call.at)
        }

    @Test
    fun invoke_marks_the_harvest_as_archived() =
        runBlocking {
            useCase(harvestId)
            assertEquals(setOf(harvestId), repository.archivedIds())
        }

    @Test
    fun invoke_does_not_change_harvest_fields() =
        runBlocking {
            useCase(harvestId)

            val harvest = repository.snapshot().single()
            assertEquals(harvestId, harvest.id)
            assertEquals(ActivityQuantityUnit.COUNT, harvest.unit)
            assertEquals(LocalDate.of(2026, 9, 15), harvest.date.date)
        }
}
