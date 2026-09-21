package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.domain.model.garden.Area

/**
 * Mapping between AreaEntity and Area domain model.
 *
 * Geometry serialisation is shared with GrowingSpace via
 * GeometryGeoJson and geometryFromEntity; this file adds no
 * geometry-specific code.
 */

fun AreaEntity.toDomain(): Area =
    Area(
        id = id,
        gardenId = garden_id,
        name = name,
        areaType = area_type,
        description = description,
        geometry = geometryFromEntity(geometry_type, geometry_data),
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
    )

fun Area.toEntity(): AreaEntity =
    AreaEntity(
        id = id,
        garden_id = gardenId,
        name = name,
        area_type = areaType,
        description = description,
        geometry_type = geometry?.geometryType,
        geometry_data = geometry?.let { GeometryGeoJson.encode(it) },
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
    )
