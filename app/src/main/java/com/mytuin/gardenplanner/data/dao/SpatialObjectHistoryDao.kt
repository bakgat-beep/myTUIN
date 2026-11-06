package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.SpatialObjectHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpatialObjectHistoryDao {
    @Insert
    suspend fun insert(history: SpatialObjectHistoryEntity)

    @Query(
        """
        SELECT * FROM spatial_object_history
        WHERE spatial_object_id = :spatialObjectId
        ORDER BY valid_from DESC
        """,
    )
    fun observeForObject(spatialObjectId: String): Flow<List<SpatialObjectHistoryEntity>>

    @Query(
        """
        SELECT * FROM spatial_object_history
        WHERE spatial_object_id = :spatialObjectId
        ORDER BY valid_from DESC
        """,
    )
    suspend fun getForObject(spatialObjectId: String): List<SpatialObjectHistoryEntity>

    @Query(
        """
        SELECT * FROM spatial_object_history
        WHERE spatial_object_id = :spatialObjectId
        ORDER BY valid_from DESC
        LIMIT 1
        """,
    )
    suspend fun getLatestForObject(spatialObjectId: String): SpatialObjectHistoryEntity?

    @Query(
        """
        SELECT * FROM spatial_object_history
        WHERE spatial_object_id = :spatialObjectId
          AND valid_from <= :atMillis
          AND valid_to > :atMillis
        LIMIT 1
        """,
    )
    suspend fun getAsOf(
        spatialObjectId: String,
        atMillis: Long,
    ): SpatialObjectHistoryEntity?
}
