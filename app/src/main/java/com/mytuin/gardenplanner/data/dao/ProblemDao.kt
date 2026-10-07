package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.ProblemEntity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ProblemDao {
    @Insert
    suspend fun insert(problem: ProblemEntity)

    @Query("SELECT * FROM problem WHERE id = :id")
    suspend fun getById(id: String): ProblemEntity?

    @Query("SELECT * FROM problem WHERE id = :id")
    fun observeById(id: String): Flow<ProblemEntity?>

    @Query(
        """
    SELECT * FROM problem
    WHERE garden_id = :gardenId
      AND (record_status IS NULL OR record_status != 'archived')
    ORDER BY updated_at DESC
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<ProblemEntity>>

    @Query(
        """
    SELECT * FROM problem
    WHERE garden_id = :gardenId
    ORDER BY updated_at DESC
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<ProblemEntity>>

    @Query(
        """
    SELECT * FROM problem
    WHERE plant_instance_id = :plantInstanceId
      AND (record_status IS NULL OR record_status != 'archived')
    ORDER BY updated_at DESC
    """,
    )
    fun observeActiveForPlantInstance(plantInstanceId: String): Flow<List<ProblemEntity>>

    @Query(
        """
    SELECT * FROM problem
    WHERE plant_instance_id = :plantInstanceId
    ORDER BY updated_at DESC
    """,
    )
    fun observeAllForPlantInstance(plantInstanceId: String): Flow<List<ProblemEntity>>

    @Query(
        """
    SELECT * FROM problem
    WHERE growing_space_id = :growingSpaceId
      AND (record_status IS NULL OR record_status != 'archived')
    ORDER BY updated_at DESC
    """,
    )
    fun observeActiveForGrowingSpace(growingSpaceId: String): Flow<List<ProblemEntity>>

    @Query(
        """
    SELECT * FROM problem
    WHERE growing_space_id = :growingSpaceId
    ORDER BY updated_at DESC
    """,
    )
    fun observeAllForGrowingSpace(growingSpaceId: String): Flow<List<ProblemEntity>>

    @Query(
        """
        UPDATE problem
        SET status = :status,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: ProblemStatus,
        updatedAt: Long,
    ): Int

    @Query(
        """
        UPDATE problem
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
