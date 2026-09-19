package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

fun GrowingSpaceEntity.toDomain(): GrowingSpace =
    GrowingSpace(
        id = id,
        gardenId = garden_id,
        name = name,
        spaceType = space_type,
        status = status,
        geometry = geometryFromEntity(geometry_type, geometry_data),
        lengthMetres = length,
        widthMetres = width,
        heightMetres = height,
        diameterMetres = diameter,
        areaSquareMetres = area,
        volumeCubicMetres = volume,
        description = description,
        notes = notes,
        createdAt = created_at,
        updatedAt = updated_at,
    )

fun GrowingSpace.toEntity(): GrowingSpaceEntity =
    GrowingSpaceEntity(
        id = id,
        garden_id = gardenId,
        name = name,
        space_type = spaceType,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        geometry_type = geometry?.geometryType,
        geometry_data = geometry?.let { GeometryGeoJson.encode(it) },
        length = lengthMetres,
        width = widthMetres,
        height = heightMetres,
        diameter = diameterMetres,
        area = areaSquareMetres,
        volume = volumeCubicMetres,
        description = description,
        notes = notes,
    )

internal fun geometryFromEntity(
    type: GeometryType?,
    data: String?,
): Geometry? {
    if (type == null && data == null) return null
    require(type != null && data != null) {
        "geometry_type and geometry_data must both be set or both be null; " +
            "got type=$type, data=${if (data == null) "null" else "present"}"
    }
    val geometry = GeometryGeoJson.decode(data)
    require(geometry.geometryType == type) {
        "Stored geometry_type '$type' does not match GeoJSON type " +
            "'${geometry.geometryType}'"
    }
    return geometry
}
