package com.mytuin.gardenplanner.domain.usecases.observation

import com.mytuin.gardenplanner.domain.model.garden.Observation
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeObservationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ArchiveObservationTest {
    private val repository = FakeObservationRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = ArchiveObservation(repository, clock)

    private val observationId = "observation_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Observation(
                    id = observationId,
                    activityId = "activity_test_0001",
                    gardenId = "garden_test_0001",
                    observedAt = 1_700_000_400_000L,
                    observationType = ObservationType.PLANT,
                    confidence = Confidence.MODERATE,
                    createdAt = 1_700_000_000_000L,
                    areaId = null,
                    growingSpaceId = null,
                    spatialObjectId = null,
                    plantInstanceId = null,
                    structuredValues = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_calls_archive_on_the_repository() =
        runBlocking {
            useCase(observationId)

            val call = repository.archiveCalls().single()
            assertEquals(observationId, call.id)
            assertEquals(1_700_000_500_000L, call.at)
        }

    @Test
    fun invoke_marks_the_observation_as_archived() =
        runBlocking {
            useCase(observationId)
            assertEquals(setOf(observationId), repository.archivedIds())
        }

    @Test
    fun invoke_does_not_change_observation_fields() =
        runBlocking {
            useCase(observationId)

            val observation = repository.snapshot().single()
            assertEquals(observationId, observation.id)
            assertEquals(ObservationType.PLANT, observation.observationType)
            assertEquals(Confidence.MODERATE, observation.confidence)
            assertEquals(1_700_000_400_000L, observation.observedAt)
        }
}
