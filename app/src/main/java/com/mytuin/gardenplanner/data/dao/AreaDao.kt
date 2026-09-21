package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaDao {
    @Insert
    suspend fun insert(area: AreaEntity)

    @Query("SELECT * FROM area WHERE id = :id")
    suspend fun getById(id: String): AreaEntity?

    /**
     * A6: status is nullable. A null status means no lifecycle state
     * recorded, which is treated as "not archived" here. The
     * explicit IS NULL check is required because SQLite evaluates
     * NULL != 'archived' as NULL, not true.
     */
    @Query(
        """
    SELECT * FROM area
    WHERE garden_id = :gardenId
      AND (status IS NULL OR status != 'archived')
    ORDER BY name
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<AreaEntity>>

    @Query(
        """
    SELECT * FROM area
    WHERE garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<AreaEntity>>

    @Query("SELECT * FROM area WHERE id = :id")
    fun observeById(id: String): Flow<AreaEntity?>

    @Query(
        """
        UPDATE area
        SET status = :status,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: RecordStatus,
        updatedAt: Long,
    ): Int
}
