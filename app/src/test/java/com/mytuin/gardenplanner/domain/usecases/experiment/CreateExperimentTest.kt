package com.mytuin.gardenplanner.domain.usecases.experiment

import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewExperiment
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeExperimentRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreateExperimentTest {
    private val repository = FakeExperimentRepository()
    private val idGenerator = FakeIdGenerator(nextExperimentIdValue = "experiment_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createExperiment =
        CreateExperiment(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createExperiment(minimalInput())
            assertEquals("experiment_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createExperiment(minimalInput())

            val experiments = repository.experimentsSnapshot()
            assertEquals(1, experiments.size)
            val experiment = experiments.first()

            assertEquals("experiment_test_0001", experiment.id)
            assertEquals("garden_test_0001", experiment.gardenId)
            assertEquals("Does mulch reduce watering?", experiment.question)
            assertEquals(ExperimentStatus.DRAFT, experiment.status)
            assertEquals(1_700_000_000_000L, experiment.createdAt)
            assertEquals(1_700_000_000_000L, experiment.updatedAt)
        }

    @Test
    fun invoke_defaults_status_to_draft() =
        runBlocking {
            createExperiment(minimalInput())
            assertEquals(ExperimentStatus.DRAFT, repository.experimentsSnapshot().first().status)
        }

    @Test
    fun invoke_defaults_optional_fields_to_null() =
        runBlocking {
            createExperiment(minimalInput())
            val experiment = repository.experimentsSnapshot().first()

            assertNull(experiment.hypothesis)
            assertNull(experiment.startDate)
            assertNull(experiment.endDate)
            assertNull(experiment.notes)
        }

    @Test
    fun invoke_preserves_hypothesis_dates_and_notes() =
        runBlocking {
            createExperiment(
                minimalInput().copy(
                    hypothesis = "Mulch will reduce watering events.",
                    startDate = KnownDate(LocalDate.of(2026, 10, 1)),
                    endDate = KnownDate(LocalDate.of(2026, 12, 31)),
                    notes = "Compare two beds of the same size.",
                ),
            )
            val experiment = repository.experimentsSnapshot().first()

            assertEquals("Mulch will reduce watering events.", experiment.hypothesis)
            assertEquals(LocalDate.of(2026, 10, 1), experiment.startDate?.date)
            assertEquals(LocalDate.of(2026, 12, 31), experiment.endDate?.date)
            assertEquals("Compare two beds of the same size.", experiment.notes)
        }

    private fun minimalInput(): NewExperiment =
        NewExperiment(
            gardenId = "garden_test_0001",
            name = "Mulch experiment",
            question = "Does mulch reduce watering?",
        )
}
