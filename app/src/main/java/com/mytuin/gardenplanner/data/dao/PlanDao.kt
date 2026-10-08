package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlanEntity
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    @Insert
    suspend fun insert(plan: PlanEntity)

    @Query("SELECT * FROM plan WHERE id = :id")
    suspend fun getById(id: String): PlanEntity?

    @Query("SELECT * FROM plan WHERE id = :id")
    fun observeById(id: String): Flow<PlanEntity?>

    @Query(
        """
    SELECT * FROM plan
    WHERE garden_id = :gardenId
      AND (record_status IS NULL OR record_status != 'archived')
    ORDER BY updated_at DESC
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<PlanEntity>>

    @Query(
        """
    SELECT * FROM plan
    WHERE garden_id = :gardenId
    ORDER BY updated_at DESC
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<PlanEntity>>

    @Query(
        """
        UPDATE plan
        SET status = :status,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: PlanningStatus,
        updatedAt: Long,
    ): Int

    @Query(
        """
        UPDATE plan
        SET record_status = :recordStatus,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateRecordStatus(
        id: String,
        recordStatus: RecordStatus,
        updatedAt: Long,
    ): Int
}
