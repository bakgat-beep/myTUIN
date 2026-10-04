package com.mytuin.gardenplanner.domain.usecases.measurement

import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewMeasurement
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementUnit
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeMeasurementRepository
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Use case tests for CreateMeasurement.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class CreateMeasurementTest {
    private val repository = FakeMeasurementRepository()
    private val idGenerator =
        FakeIdGenerator(nextMeasurementIdValue = "measurement_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createMeasurement =
        CreateMeasurement(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createMeasurement(minimalInput())
            assertEquals("measurement_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_and_createdAt() =
        runBlocking {
            createMeasurement(minimalInput())

            val measurements = repository.snapshot()
            assertEquals(1, measurements.size)
            val measurement = measurements.first()

            assertEquals("measurement_test_0001", measurement.id)
            assertEquals("garden_test_0001", measurement.gardenId)
            assertEquals(MeasurementProperty.PH, measurement.property)
            assertEquals(6.7, measurement.value, 0.0)
            assertEquals(MeasurementUnit.PH, measurement.unit)
            assertEquals(Confidence.NOT_ASSESSED, measurement.confidence)
            assertEquals(1_700_000_000_000L, measurement.createdAt)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createMeasurement(minimalInput())
            val measurement = repository.snapshot().first()

            assertNull(measurement.areaId)
            assertNull(measurement.growingSpaceId)
            assertNull(measurement.spatialObjectId)
            assertNull(measurement.plantInstanceId)
            assertNull(measurement.notes)
        }

    @Test
    fun invoke_preserves_all_optional_parent_references() =
        runBlocking {
            createMeasurement(
                minimalInput().copy(
                    areaId = "area_test_0001",
                    growingSpaceId = "growingspace_test_0001",
                    spatialObjectId = "spatialobject_test_0001",
                    plantInstanceId = "plantinstance_test_0001",
                ),
            )
            val measurement = repository.snapshot().first()

            assertEquals("area_test_0001", measurement.areaId)
            assertEquals("growingspace_test_0001", measurement.growingSpaceId)
            assertEquals("spatialobject_test_0001", measurement.spatialObjectId)
            assertEquals("plantinstance_test_0001", measurement.plantInstanceId)
        }

    @Test
    fun invoke_preserves_date_only_known_date() =
        runBlocking {
            createMeasurement(
                minimalInput().copy(
                    measuredAt = KnownDate(LocalDate.of(2026, 9, 15)),
                ),
            )
            val measurement = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), measurement.measuredAt.date)
            assertNull(measurement.measuredAt.time)
        }

    @Test
    fun invoke_preserves_timestamp_known_date() =
        runBlocking {
            createMeasurement(
                minimalInput().copy(
                    measuredAt =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(10, 30),
                        ),
                ),
            )
            val measurement = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 9, 15), measurement.measuredAt.date)
            assertEquals(LocalTime.of(10, 30), measurement.measuredAt.time)
        }

    @Test
    fun invoke_preserves_confidence_and_notes() =
        runBlocking {
            createMeasurement(
                minimalInput().copy(
                    confidence = Confidence.HIGH,
                    notes = "Home test kit",
                ),
            )
            val measurement = repository.snapshot().first()

            assertEquals(Confidence.HIGH, measurement.confidence)
            assertEquals("Home test kit", measurement.notes)
        }

    @Test
    fun invoke_accepts_all_seventeen_properties() =
        runBlocking {
            MeasurementProperty.entries.forEach { property ->
                createMeasurement(minimalInput().copy(property = property))
            }
            assertEquals(17, repository.snapshot().size)
            assertEquals(
                MeasurementProperty.entries.toSet(),
                repository.snapshot().map { it.property }.toSet(),
            )
        }

    @Test
    fun invoke_accepts_all_fourteen_units() =
        runBlocking {
            MeasurementUnit.entries.forEach { unit ->
                createMeasurement(minimalInput().copy(unit = unit))
            }
            assertEquals(14, repository.snapshot().size)
            assertEquals(
                MeasurementUnit.entries.toSet(),
                repository.snapshot().map { it.unit }.toSet(),
            )
        }

    @Test
    fun invoke_accepts_all_six_confidence_values() =
        runBlocking {
            Confidence.entries.forEach { confidence ->
                createMeasurement(minimalInput().copy(confidence = confidence))
            }
            assertEquals(6, repository.snapshot().size)
        }

    private fun minimalInput(): NewMeasurement =
        NewMeasurement(
            gardenId = "garden_test_0001",
            property = MeasurementProperty.PH,
            value = 6.7,
            unit = MeasurementUnit.PH,
            measuredAt = KnownDate(LocalDate.of(2026, 9, 15)),
        )
}
