package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.HarvestEntity
import com.mytuin.gardenplanner.domain.model.garden.Harvest
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import java.time.ZoneOffset

/**
 * Mapping between HarvestEntity and Harvest domain model.
 *
 * The KnownDate pair is decoded via knownDateFromEntity and encoded
 * via encodeTime, both shared with PlantInstanceMapper.
 */

fun HarvestEntity.toDomain(): Harvest =
    Harvest(
        id = id,
        activityId = activity_id,
        gardenId = garden_id,
        date =
            requireNotNull(
                knownDateFromEntity(date_epoch_day, date_time_of_day_millis),
            ) {
                "date_epoch_day must be non-null; a harvest always has a date"
            },
        quantity = quantity,
        unit = unit,
        createdAt = created_at,
        sizeCategory = size_category,
        plantInstanceId = plant_instance_id,
        growingSpaceId = growing_space_id,
        notes = notes,
    )

fun Harvest.toEntity(): HarvestEntity =
    HarvestEntity(
        id = id,
        garden_id = gardenId,
        activity_id = activityId,
        date_epoch_day = date.date.toEpochDay().toInt(),
        date_time_of_day_millis = date.encodeTime(),
        quantity = quantity,
        unit = unit,
        created_at = createdAt,
        size_category = sizeCategory,
        plant_instance_id = plantInstanceId,
        growing_space_id = growingSpaceId,
        notes = notes,
    )

/**
 * The linked Activity's occurred_at, derived from the Harvest's date.
 *
 * Date-only KnownDates are normalized to UTC midnight of that day.
 * This is a deterministic convention: the Harvest row itself retains
 * the null time; only the Activity's timestamp representation is
 * normalized. The Activity's KDoc records this so the pipeline is
 * auditable.
 *
 * See HarvestRepositoryImpl.activityFor.
 */
internal fun KnownDate.toActivityOccurredAtMillis(): Long =
    time
        ?.let {
            date
                .atTime(it)
                .atZone(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli()
        }
        ?: date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
