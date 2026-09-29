package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantInstanceHistoryEntity
import com.mytuin.gardenplanner.domain.model.garden.PlantInstanceHistory

/**
 * Mapping between PlantInstanceHistoryEntity and PlantInstanceHistory
 * domain model. One-way: history rows are created by
 * PlantInstanceRepositoryImpl and read via this mapper.
 */

fun PlantInstanceHistoryEntity.toDomain(): PlantInstanceHistory =
    PlantInstanceHistory(
        id = id,
        plantInstanceId = plant_instance_id,
        growingSpaceId = growing_space_id,
        spatialObjectId = spatial_object_id,
        geometry = geometryFromEntity(geometry_type, geometry_data),
        validFrom = valid_from,
        validTo = valid_to,
        recordedAt = recorded_at,
        reason = reason,
    )
