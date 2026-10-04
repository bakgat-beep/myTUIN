package com.mytuin.gardenplanner.domain.usecases.measurement

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Measurement
import com.mytuin.gardenplanner.domain.model.garden.NewMeasurement
import com.mytuin.gardenplanner.domain.repository.MeasurementRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Record a new Measurement.
 *
 * Assigns id and createdAt. Returns the new id.
 *
 * No transaction. A single-row insert does not need one (M1 (a): no
 * Activity companion record).
 */
class CreateMeasurement
    @Inject
    constructor(
        private val repository: MeasurementRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewMeasurement): String {
            val measurement =
                Measurement(
                    id = idGenerator.newMeasurementId(),
                    gardenId = input.gardenId,
                    property = input.property,
                    value = input.value,
                    unit = input.unit,
                    measuredAt = input.measuredAt,
                    confidence = input.confidence,
                    createdAt = clock.nowMillis(),
                    areaId = input.areaId,
                    growingSpaceId = input.growingSpaceId,
                    spatialObjectId = input.spatialObjectId,
                    plantInstanceId = input.plantInstanceId,
                    notes = input.notes,
                )
            repository.insert(measurement)
            return measurement.id
        }
    }
