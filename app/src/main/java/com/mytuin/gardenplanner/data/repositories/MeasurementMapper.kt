package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.MeasurementEntity
import com.mytuin.gardenplanner.domain.model.garden.Measurement

/**
 * Mapping between MeasurementEntity and Measurement domain model.
 *
 * The KnownDate pair is decoded via knownDateFromEntity and encoded
 * via encodeTime, both shared with PlantInstanceMapper. The
 * measured_at_epoch_day column is required, so the decoder is called
 * with a non-null day and a nullable time.
 */

fun MeasurementEntity.toDomain(): Measurement =
    Measurement(
        id = id,
        gardenId = garden_id,
        property = property,
        value = value,
        unit = unit,
        measuredAt =
            requireNotNull(
                knownDateFromEntity(measured_at_epoch_day, measured_at_time_of_day_millis),
            ) {
                "measured_at_epoch_day must be non-null; a measurement always has a date"
            },
        confidence = confidence,
        createdAt = created_at,
        areaId = area_id,
        growingSpaceId = growing_space_id,
        spatialObjectId = spatial_object_id,
        plantInstanceId = plant_instance_id,
        notes = notes,
    )

fun Measurement.toEntity(): MeasurementEntity =
    MeasurementEntity(
        id = id,
        garden_id = gardenId,
        property = property,
        value = value,
        unit = unit,
        measured_at_epoch_day = measuredAt.date.toEpochDay().toInt(),
        measured_at_time_of_day_millis = measuredAt.encodeTime(),
        confidence = confidence,
        created_at = createdAt,
        area_id = areaId,
        growing_space_id = growingSpaceId,
        spatial_object_id = spatialObjectId,
        plant_instance_id = plantInstanceId,
        notes = notes,
    )
