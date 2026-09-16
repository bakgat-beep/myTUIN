package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GrowingSpaceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowingSpaceHistoryDao {

    @Insert
    suspend fun insert(history: GrowingSpaceHistoryEntity)

    @Query(
        """
        SELECT * FROM growing_space_history
        WHERE growing_space_id = :growingSpaceId
        ORDER BY valid_from DESC
        """
    )
    fun observeForSpace(growingSpaceId: String): Flow<List<GrowingSpaceHistoryEntity>>

    @Query(
        """
        SELECT * FROM growing_space_history
        WHERE growing_space_id = :growingSpaceId
        ORDER BY valid_from DESC
        """
    )
    suspend fun getForSpace(growingSpaceId: String): List<GrowingSpaceHistoryEntity>

    @Query(
        """
        SELECT * FROM growing_space_history
        WHERE growing_space_id = :growingSpaceId
        ORDER BY valid_from DESC
        LIMIT 1
        """
    )
    suspend fun getLatestForSpace(growingSpaceId: String): GrowingSpaceHistoryEntity?

    @Query(
        """
        SELECT * FROM growing_space_history
        WHERE growing_space_id = :growingSpaceId
          AND valid_from <= :atMillis
          AND valid_to > :atMillis
        LIMIT 1
        """
    )
    suspend fun getAsOf(growingSpaceId: String, atMillis: Long): GrowingSpaceHistoryEntity?
}