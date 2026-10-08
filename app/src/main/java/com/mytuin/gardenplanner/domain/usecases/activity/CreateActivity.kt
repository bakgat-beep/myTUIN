package com.mytuin.gardenplanner.domain.usecases.activity

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Activity
import com.mytuin.gardenplanner.domain.model.garden.NewActivity
import com.mytuin.gardenplanner.domain.repository.ActivityRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new Activity.
 *
 * Assigns id, status (ACTIVE) and createdAt. occurredAt is caller-
 * supplied; the event time is part of the record's meaning.
 *
 * PL9: `planId` is passed through if supplied, linking this Activity
 * to the Plan it completes. Nullable; most Activities are not
 * completions.
 *
 * All six foreign keys (garden, area, growing space, spatial object,
 * plant instance, plan) are enforced by Room. Missing parents
 * surface as ValidationError with the offending field named.
 */
class CreateActivity
    @Inject
    constructor(
        private val repository: ActivityRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewActivity): String {
            val now = clock.nowMillis()
            val activity =
                Activity(
                    id = idGenerator.newActivityId(),
                    gardenId = input.gardenId,
                    activityType = input.activityType,
                    occurredAt = input.occurredAt,
                    createdAt = now,
                    areaId = input.areaId,
                    growingSpaceId = input.growingSpaceId,
                    spatialObjectId = input.spatialObjectId,
                    plantInstanceId = input.plantInstanceId,
                    planId = input.planId,
                    quantity = input.quantity,
                    unit = input.unit,
                    detail = input.detail,
                    dataOrigin = input.dataOrigin,
                    status = RecordStatus.ACTIVE,
                    notes = input.notes,
                )
            repository.insert(activity)
            return activity.id
        }
    }
