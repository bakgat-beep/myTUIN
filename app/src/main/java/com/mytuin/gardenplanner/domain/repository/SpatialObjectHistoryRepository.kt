package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for SpatialObjectHistory.
 *
 * DEC-041 / DEC-051. History rows are read-only from the domain's
 * perspective; they are created by
 * SpatialObjectRepository.updateGeometry inside a transaction, not
 * by direct calls from use cases.
 */
interface SpatialObjectHistoryRepository {
    fun observeHistoryForObject(spatialObjectId: String): Flow<List<SpatialObjectHistory>>

    suspend fun getHistoryForObject(spatialObjectId: String): List<SpatialObjectHistory>

    /**
     * The state that was effective at [atMillis], or null if no
     * history row covers that instant.
     */
    suspend fun getHistoryAsOf(
        spatialObjectId: String,
        atMillis: Long,
    ): SpatialObjectHistory?
}
