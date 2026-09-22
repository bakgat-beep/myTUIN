package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

/**
 * Mapping between SpatialObjectEntity and SpatialObject domain model.
 *
 * Unlike GrowingSpace and Area, geometry is required (A5), so this
 * file uses a strict decoder rather than the shared nullable
 * geometryFromEntity. The GeoJSON type-name cross-check is the same.
 */

fun SpatialObjectEntity.toDomain(): SpatialObject =
    SpatialObject(
        id = id,
        gardenId = garden_id,
        objectType = object_type,
        geometry = requiredGeometry(geometry_type, geometry_data),
        status = status,
        name = name,
        description = description,
        areaId = area_id,
        notes = notes,
        createdAt = created_at,
        updatedAt = updated_at,
    )

fun SpatialObject.toEntity(): SpatialObjectEntity =
    SpatialObjectEntity(
        id = id,
        garden_id = gardenId,
        object_type = objectType,
        geometry_type = geometry.geometryType,
        geometry_data = GeometryGeoJson.encode(geometry),
        status = status,
        name = name,
        description = description,
        area_id = areaId,
        notes = notes,
        created_at = createdAt,
        updated_at = updatedAt,
    )

private fun requiredGeometry(
    type: GeometryType,
    data: String,
): Geometry {
    val geometry = GeometryGeoJson.decode(data)
    require(geometry.geometryType == type) {
        "Stored geometry_type '$type' does not match GeoJSON type " +
            "'${geometry.geometryType}'"
    }
    return geometry
}
