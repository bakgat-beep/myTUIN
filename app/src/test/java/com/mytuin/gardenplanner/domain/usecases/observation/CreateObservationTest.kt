package com.mytuin.gardenplanner.domain.usecases.observation

import com.mytuin.gardenplanner.domain.model.garden.NewObservation
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeObservationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateObservation.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateObservationTest {
    private val repository = FakeObservationRepository()
    private val idGenerator =
        FakeIdGenerator(
            nextObservationIdValue = "observation_test_0001",
            nextActivityIdValue = "activity_test_0001",
        )
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createObservation =
        CreateObservation(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_observation_id() =
        runBlocking {
            val id = createObservation(minimalInput())
            assertEquals("observation_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_ids_status_and_timestamps() =
        runBlocking {
            createObservation(minimalInput())

            val observations = repository.snapshot()
            assertEquals(1, observations.size)
            val observation = observations.first()

            assertEquals("observation_test_0001", observation.id)
            assertEquals("activity_test_0001", observation.activityId)
            assertEquals("garden_test_0001", observation.gardenId)
            assertEquals(ObservationType.PLANT, observation.observationType)
            assertEquals(Confidence.NOT_ASSESSED, observation.confidence)
            assertEquals(1_700_000_500_000L, observation.observedAt)
            assertEquals(1_700_000_000_000L, observation.createdAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createObservation(minimalInput())
            val observation = repository.snapshot().first()

            assertNull(observation.areaId)
            assertNull(observation.growingSpaceId)
            assertNull(observation.spatialObjectId)
            assertNull(observation.plantInstanceId)
            assertNull(observation.structuredValues)
            assertNull(observation.notes)
        }

    @Test
    fun invoke_preserves_all_optional_parent_references() =
        runBlocking {
            createObservation(
                minimalInput().copy(
                    areaId = "area_test_0001",
                    growingSpaceId = "growingspace_test_0001",
                    spatialObjectId = "spatialobject_test_0001",
                    plantInstanceId = "plantinstance_test_0001",
                ),
            )
            val observation = repository.snapshot().first()

            assertEquals("area_test_0001", observation.areaId)
            assertEquals("growingspace_test_0001", observation.growingSpaceId)
            assertEquals("spatialobject_test_0001", observation.spatialObjectId)
            assertEquals("plantinstance_test_0001", observation.plantInstanceId)
        }

    @Test
    fun invoke_preserves_structured_values_and_notes() =
        runBlocking {
            createObservation(
                minimalInput().copy(
                    structuredValues = """{"moisture":"dry"}""",
                    notes = "Soil feels dry to the touch",
                ),
            )
            val observation = repository.snapshot().first()

            assertEquals("""{"moisture":"dry"}""", observation.structuredValues)
            assertEquals("Soil feels dry to the touch", observation.notes)
        }

    @Test
    fun invoke_accepts_all_thirteen_observation_types() =
        runBlocking {
            ObservationType.entries.forEach { type ->
                createObservation(minimalInput().copy(observationType = type))
            }
            assertEquals(13, repository.snapshot().size)
            assertEquals(
                ObservationType.entries.toSet(),
                repository.snapshot().map { it.observationType }.toSet(),
            )
        }

    @Test
    fun invoke_accepts_all_six_confidence_values() =
        runBlocking {
            Confidence.entries.forEach { confidence ->
                createObservation(minimalInput().copy(confidence = confidence))
            }
            assertEquals(6, repository.snapshot().size)
            assertEquals(
                Confidence.entries.toSet(),
                repository.snapshot().map { it.confidence }.toSet(),
            )
        }

    private fun minimalInput(): NewObservation =
        NewObservation(
            gardenId = "garden_test_0001",
            observedAt = 1_700_000_500_000L,
            observationType = ObservationType.PLANT,
        )
}
