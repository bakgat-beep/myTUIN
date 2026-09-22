package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for SpatialObject.
 *
 * Mirrors AreaRepository. No updateGeometry yet: no requirement, and
 * adding it would imply a history-table decision DEC-041 does not
 * currently make for SpatialObject (A4).
 */
interface SpatialObjectRepository {
    fun observeSpatialObjectsInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<SpatialObject>>

    fun observeSpatialObject(id: String): Flow<SpatialObject?>

    suspend fun getSpatialObject(id: String): SpatialObject?

    suspend fun insert(spatialObject: SpatialObject)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )
}
