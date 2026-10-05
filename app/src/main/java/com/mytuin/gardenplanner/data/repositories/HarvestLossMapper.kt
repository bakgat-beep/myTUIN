package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.HarvestLossEntity
import com.mytuin.gardenplanner.domain.model.garden.HarvestLoss

/**
 * Mapping between HarvestLossEntity and HarvestLoss domain model.
 *
 * The KnownDate pair is decoded via knownDateFromEntity and encoded
 * via encodeTime, both shared with PlantInstanceMapper.
 */

fun HarvestLossEntity.toDomain(): HarvestLoss =
    HarvestLoss(
        id = id,
        gardenId = garden_id,
        date =
            requireNotNull(
                knownDateFromEntity(date_epoch_day, date_time_of_day_millis),
            ) {
                "date_epoch_day must be non-null; a harvest loss always has a date"
            },
        quantity = quantity,
        unit = unit,
        createdAt = created_at,
        activityId = activity_id,
        cause = cause,
        severity = severity,
        plantInstanceId = plant_instance_id,
        growingSpaceId = growing_space_id,
        notes = notes,
    )

fun HarvestLoss.toEntity(): HarvestLossEntity =
    HarvestLossEntity(
        id = id,
        garden_id = gardenId,
        date_epoch_day = date.date.toEpochDay().toInt(),
        date_time_of_day_millis = date.encodeTime(),
        quantity = quantity,
        unit = unit,
        created_at = createdAt,
        activity_id = activityId,
        cause = cause,
        severity = severity,
        plant_instance_id = plantInstanceId,
        growing_space_id = growingSpaceId,
        notes = notes,
    )
