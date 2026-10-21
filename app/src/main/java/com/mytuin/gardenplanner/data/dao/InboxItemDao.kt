package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.InboxItemEntity
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface InboxItemDao {
    @Insert
    suspend fun insert(inboxItem: InboxItemEntity)

    @Query("SELECT * FROM inbox_item WHERE id = :id")
    suspend fun getById(id: String): InboxItemEntity?

    @Query("SELECT * FROM inbox_item WHERE id = :id")
    fun observeById(id: String): Flow<InboxItemEntity?>

    @Query(
        """
    SELECT * FROM inbox_item
    WHERE garden_id = :gardenId AND status = 'pending'
    ORDER BY created_at DESC
    """,
    )
    fun observePendingForGarden(gardenId: String): Flow<List<InboxItemEntity>>

    @Query(
        """
    SELECT * FROM inbox_item
    WHERE garden_id = :gardenId
    ORDER BY created_at DESC
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<InboxItemEntity>>

    @Query(
        """
        UPDATE inbox_item
        SET status = :status,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: InboxItemStatus,
        updatedAt: Long,
    ): Int

    @Query(
        """
        UPDATE inbox_item
        SET muted_until = :mutedUntil,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateMutedUntil(
        id: String,
        mutedUntil: Long?,
        updatedAt: Long,
    ): Int
}
