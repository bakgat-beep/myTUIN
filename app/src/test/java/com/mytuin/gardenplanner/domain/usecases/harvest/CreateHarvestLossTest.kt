package com.mytuin.gardenplanner.domain.usecases.harvest

import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewHarvestLoss
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossCause
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossSeverity
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeHarvestLossRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateHarvestLoss.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateHarvestLossTest {
    private val repository = FakeHarvestLossRepository()
    private val idGenerator =
        FakeIdGenerator(nextHarvestLossIdValue = "harvestloss_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createHarvestLoss =
        CreateHarvestLoss(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createHarvestLoss(minimalInput())
            assertEquals("harvestloss_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_and_createdAt() =
        runBlocking {
            createHarvestLoss(minimalInput())

            val losses = repository.snapshot()
            assertEquals(1, losses.size)
            val loss = losses.first()

            assertEquals("harvestloss_test_0001", loss.id)
            assertEquals("garden_test_0001", loss.gardenId)
            assertEquals(1_700_000_000_000L, loss.createdAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createHarvestLoss(minimalInput())
            val loss = repository.snapshot().first()

            assertNull(loss.activityId)
            assertNull(loss.cause)
            assertNull(loss.severity)
            assertNull(loss.plantInstanceId)
            assertNull(loss.growingSpaceId)
            assertNull(loss.notes)
        }

    @Test
    fun invoke_preserves_optional_activity_id() =
        runBlocking {
            createHarvestLoss(minimalInput().copy(activityId = "activity_test_0001"))
            assertEquals("activity_test_0001", repository.snapshot().first().activityId)
        }

    @Test
    fun invoke_preserves_cause_and_severity() =
        runBlocking {
            createHarvestLoss(
                minimalInput().copy(
                    cause = HarvestLossCause.ROT,
                    severity = HarvestLossSeverity.MODERATE,
                ),
            )
            val loss = repository.snapshot().first()

            assertEquals(HarvestLossCause.ROT, loss.cause)
            assertEquals(HarvestLossSeverity.MODERATE, loss.severity)
        }

    @Test
    fun invoke_preserves_plant_instance_and_growing_space_references() =
        runBlocking {
            createHarvestLoss(
                minimalInput().copy(
                    plantInstanceId = "plantinstance_test_0001",
                    growingSpaceId = "growingspace_test_0001",
                ),
            )
            val loss = repository.snapshot().first()

            assertEquals("plantinstance_test_0001", loss.plantInstanceId)
            assertEquals("growingspace_test_0001", loss.growingSpaceId)
        }

    @Test
    fun invoke_preserves_date_only_known_date() =
        runBlocking {
            createHarvestLoss(
                minimalInput().copy(
                    date = KnownDate(LocalDate.of(2026, 9, 15)),
                ),
            )
            val loss = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), loss.date.date)
            assertNull(loss.date.time)
        }

    @Test
    fun invoke_preserves_timestamp_known_date() =
        runBlocking {
            createHarvestLoss(
                minimalInput().copy(
                    date =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(8, 0),
                        ),
                ),
            )
            val loss = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), loss.date.date)
            assertEquals(LocalTime.of(8, 0), loss.date.time)
        }

    @Test
    fun invoke_accepts_all_nine_causes() =
        runBlocking {
            HarvestLossCause.entries.forEach { cause ->
                createHarvestLoss(minimalInput().copy(cause = cause))
            }
            assertEquals(9, repository.snapshot().size)
            assertEquals(
                HarvestLossCause.entries.toSet(),
                repository.snapshot().mapNotNull { it.cause }.toSet(),
            )
        }

    @Test
    fun invoke_accepts_all_five_severities() =
        runBlocking {
            HarvestLossSeverity.entries.forEach { severity ->
                createHarvestLoss(minimalInput().copy(severity = severity))
            }
            assertEquals(5, repository.snapshot().size)
            assertEquals(
                HarvestLossSeverity.entries.toSet(),
                repository.snapshot().mapNotNull { it.severity }.toSet(),
            )
        }

    private fun minimalInput(): NewHarvestLoss =
        NewHarvestLoss(
            gardenId = "garden_test_0001",
            date = KnownDate(LocalDate.of(2026, 9, 15)),
            quantity = 3.0,
            unit = ActivityQuantityUnit.COUNT,
        )
}
