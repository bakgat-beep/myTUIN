package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.ObservationEntity
import com.mytuin.gardenplanner.domain.model.garden.Observation

/**
 * Mapping between ObservationEntity and Observation domain model.
 *
 * Bidirectional. No invariants enforced here beyond field
 * correspondence: ObservationEntity has one subtype-free shape, and
 * the domain model mirrors it. The activity_id link is carried as a
 * plain field, set by CreateObservation's use case (S1 (ii)).
 */

fun ObservationEntity.toDomain(): Observation =
    Observation(
        id = id,
        activityId = activity_id,
        gardenId = garden_id,
        observedAt = observed_at,
        observationType = observation_type,
        confidence = confidence,
        createdAt = created_at,
        areaId = area_id,
        growingSpaceId = growing_space_id,
        spatialObjectId = spatial_object_id,
        plantInstanceId = plant_instance_id,
        structuredValues = structured_values,
        notes = notes,
    )

fun Observation.toEntity(): ObservationEntity =
    ObservationEntity(
        id = id,
        garden_id = gardenId,
        activity_id = activityId,
        observed_at = observedAt,
        observation_type = observationType,
        confidence = confidence,
        created_at = createdAt,
        area_id = areaId,
        growing_space_id = growingSpaceId,
        spatial_object_id = spatialObjectId,
        plant_instance_id = plantInstanceId,
        structured_values = structuredValues,
        notes = notes,
    )
