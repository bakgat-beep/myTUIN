package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.SpatialObjectHistoryEntity
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

/**
 * Mapping between SpatialObjectHistoryEntity and
 * SpatialObjectHistory domain model.
 *
 * Unlike GrowingSpaceHistoryMapper, geometry is required (A5), so
 * this file uses the strict decoder (same pattern as
 * SpatialObjectMapper.requiredGeometry). The GeoJSON type-name
 * cross-check is the same.
 */
fun SpatialObjectHistoryEntity.toDomain(): SpatialObjectHistory =
    SpatialObjectHistory(
        id = id,
        spatialObjectId = spatial_object_id,
        geometry = requiredHistoryGeometry(geometry_type, geometry_data),
        validFrom = valid_from,
        validTo = valid_to,
        recordedAt = recorded_at,
        reason = reason,
    )

private fun requiredHistoryGeometry(
    type: GeometryType,
    data: String,
) = GeometryGeoJson.decode(data).also { geometry ->
    require(geometry.geometryType == type) {
        "Stored geometry_type '$type' does not match GeoJSON type '${geometry.geometryType}'"
    }
}
