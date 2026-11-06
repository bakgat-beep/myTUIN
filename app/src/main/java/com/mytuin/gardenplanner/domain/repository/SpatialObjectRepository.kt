package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for SpatialObject.
 *
 * Two update paths, mirroring GrowingSpaceRepository:
 *   - updateGeometry writes the current geometry and a companion
 *     history row atomically (DEC-041 / DEC-051). newGeometry is
 *     non-null because SpatialObject.geometry is required (A5).
 *   - updateFields writes only the mutable scalar fields; geometry,
 *     status, garden_id and created_at are preserved by the DAO
 *     query.
 */
interface SpatialObjectRepository {
    fun observeSpatialObjectsInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<SpatialObject>>

    fun observeSpatialObject(id: String): Flow<SpatialObject?>

    suspend fun getSpatialObject(id: String): SpatialObject?

    suspend fun insert(spatialObject: SpatialObject)

    suspend fun updateGeometry(
        id: String,
        newGeometry: Geometry,
        effectiveAt: Long,
        reason: String?,
    )

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )

    suspend fun updateFields(
        id: String,
        fields: SpatialObjectFields,
        updatedAt: Long,
    )
}
