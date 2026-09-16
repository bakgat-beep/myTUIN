package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.GrowingSpaceHistoryEntity
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory

fun GrowingSpaceHistoryEntity.toDomain(): GrowingSpaceHistory = GrowingSpaceHistory(
    id = id,
    growingSpaceId = growing_space_id,
    geometry = geometryFromEntity(geometry_type, geometry_data),
    lengthMetres = length,
    widthMetres = width,
    heightMetres = height,
    diameterMetres = diameter,
    areaSquareMetres = area,
    volumeCubicMetres = volume,
    validFrom = valid_from,
    validTo = valid_to,
    recordedAt = recorded_at,
    reason = reason,
)