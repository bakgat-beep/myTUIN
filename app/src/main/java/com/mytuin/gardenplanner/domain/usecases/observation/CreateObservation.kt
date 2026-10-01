package com.mytuin.gardenplanner.domain.usecases.observation

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewObservation
import com.mytuin.gardenplanner.domain.model.garden.Observation
import com.mytuin.gardenplanner.domain.repository.ObservationRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Record a new Observation.
 *
 * S1 (ii): generates two ids — one for the Observation, one for the
 * linked timeline Activity — and passes a fully-formed Observation
 * to the repository, which writes both rows in one transaction.
 *
 * The Activity's content is derived from the Observation:
 *   activity_type = observation
 *   occurred_at   = observedAt
 *   location refs = the same as the Observation's
 *   status        = ACTIVE
 *   notes         = null (the note lives on the Observation)
 *
 * Returns the new Observation's id.
 */
class CreateObservation
    @Inject
    constructor(
        private val repository: ObservationRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewObservation): String {
            val now = clock.nowMillis()
            val observation =
                Observation(
                    id = idGenerator.newObservationId(),
                    activityId = idGenerator.newActivityId(),
                    gardenId = input.gardenId,
                    observedAt = input.observedAt,
                    observationType = input.observationType,
                    confidence = input.confidence,
                    createdAt = now,
                    areaId = input.areaId,
                    growingSpaceId = input.growingSpaceId,
                    spatialObjectId = input.spatialObjectId,
                    plantInstanceId = input.plantInstanceId,
                    structuredValues = input.structuredValues,
                    notes = input.notes,
                )
            repository.insert(observation)
            return observation.id
        }
    }
