package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.SourceEntity
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import kotlinx.coroutines.flow.Flow

/**
 * Source has no garden scope (SC1) and no status-change history.
 * Queries return all sources, ordered by creation time descending.
 */
@Dao
interface SourceDao {
    @Insert
    suspend fun insert(source: SourceEntity)

    @Query("SELECT * FROM source WHERE id = :id")
    suspend fun getById(id: String): SourceEntity?

    @Query("SELECT * FROM source WHERE id = :id")
    fun observeById(id: String): Flow<SourceEntity?>

    @Query("SELECT * FROM source ORDER BY created_at DESC")
    fun observeAll(): Flow<List<SourceEntity>>

    @Query(
        """
        UPDATE source
        SET status = :status
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: SourceStatus,
    ): Int
}
