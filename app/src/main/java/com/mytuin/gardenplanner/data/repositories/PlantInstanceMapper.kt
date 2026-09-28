package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import java.time.LocalDate
import java.time.LocalTime

/**
 * Mapping between PlantInstanceEntity and PlantInstance domain model.
 *
 * The four KnownDate fields are each stored as a pair of nullable
 * columns. This file is the single place where the pair <-> KnownDate
 * translation happens, and the single place where the pairing
 * invariant (time non-null implies day non-null) is enforced.
 *
 * Geometry uses the shared geometryFromEntity and GeometryGeoJson
 * helpers, unchanged.
 */

private const val MILLIS_PER_SECOND = 1000

fun PlantInstanceEntity.toDomain(): PlantInstance =
    PlantInstance(
        id = id,
        gardenId = garden_id,
        plantId = plant_id,
        status = status,
        cultivarId = cultivar_id,
        growingSpaceId = growing_space_id,
        spatialObjectId = spatial_object_id,
        name = name,
        quantity = quantity,
        plannedDate = knownDateFromEntity(planned_date_epoch_day, planned_date_time_of_day_millis),
        plantedDate = knownDateFromEntity(planted_date_epoch_day, planted_date_time_of_day_millis),
        expectedEndDate = knownDateFromEntity(expected_end_date_epoch_day, expected_end_date_time_of_day_millis),
        removedDate = knownDateFromEntity(removed_date_epoch_day, removed_date_time_of_day_millis),
        plantingStock = planting_stock,
        lifecycle = lifecycle,
        geometry = geometryFromEntity(geometry_type, geometry_data),
        notes = notes,
        createdAt = created_at,
        updatedAt = updated_at,
    )

fun PlantInstance.toEntity(): PlantInstanceEntity =
    PlantInstanceEntity(
        id = id,
        garden_id = gardenId,
        plant_id = plantId,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        cultivar_id = cultivarId,
        growing_space_id = growingSpaceId,
        spatial_object_id = spatialObjectId,
        name = name,
        quantity = quantity,
        planned_date_epoch_day = plannedDate?.date?.toEpochDay()?.toInt(),
        planned_date_time_of_day_millis = plannedDate.encodeTime(),
        planted_date_epoch_day = plantedDate?.date?.toEpochDay()?.toInt(),
        planted_date_time_of_day_millis = plantedDate.encodeTime(),
        expected_end_date_epoch_day = expectedEndDate?.date?.toEpochDay()?.toInt(),
        expected_end_date_time_of_day_millis = expectedEndDate.encodeTime(),
        removed_date_epoch_day = removedDate?.date?.toEpochDay()?.toInt(),
        removed_date_time_of_day_millis = removedDate.encodeTime(),
        planting_stock = plantingStock,
        lifecycle = lifecycle,
        geometry_type = geometry?.geometryType,
        geometry_data = geometry?.let { GeometryGeoJson.encode(it) },
        notes = notes,
    )

private fun KnownDate?.encodeTime(): Int? = this?.time?.toSecondOfDay()?.let { it * MILLIS_PER_SECOND }

internal fun knownDateFromEntity(
    epochDay: Int?,
    timeOfDayMillis: Int?,
): KnownDate? {
    if (epochDay == null) {
        require(timeOfDayMillis == null) {
            "time_of_day_millis must be null when epoch_day is null; " +
                "got epochDay=null, timeOfDayMillis=$timeOfDayMillis"
        }
        return null
    }
    return KnownDate(
        date = LocalDate.ofEpochDay(epochDay.toLong()),
        time = timeOfDayMillis?.let { LocalTime.ofSecondOfDay((it / MILLIS_PER_SECOND).toLong()) },
    )
}
