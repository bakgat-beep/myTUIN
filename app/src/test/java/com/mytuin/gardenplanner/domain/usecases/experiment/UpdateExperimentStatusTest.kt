package com.mytuin.gardenplanner.domain.usecases.experiment

import com.mytuin.gardenplanner.domain.model.garden.Experiment
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeExperimentRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateExperimentStatusTest {
    private val repository = FakeExperimentRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = UpdateExperimentStatus(repository, clock)

    private val experimentId = "experiment_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Experiment(
                    id = experimentId,
                    gardenId = "garden_test_0001",
                    name = "Mulch experiment",
                    question = "Does mulch reduce watering?",
                    status = ExperimentStatus.DRAFT,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                    hypothesis = null,
                    startDate = null,
                    endDate = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_updates_the_status() =
        runBlocking {
            useCase(experimentId, ExperimentStatus.ACTIVE)
            assertEquals(ExperimentStatus.ACTIVE, repository.experimentsSnapshot().single().status)
        }

    @Test
    fun invoke_supplies_updatedAt_from_clock() =
        runBlocking {
            useCase(experimentId, ExperimentStatus.COMPLETED)
            assertEquals(1_700_000_500_000L, repository.experimentsSnapshot().single().updatedAt)
        }

    @Test
    fun invoke_does_not_change_id_or_createdAt() =
        runBlocking {
            useCase(experimentId, ExperimentStatus.ACTIVE)
            val experiment = repository.experimentsSnapshot().single()
            assertEquals(experimentId, experiment.id)
            assertEquals(1_700_000_000_000L, experiment.createdAt)
        }
}
