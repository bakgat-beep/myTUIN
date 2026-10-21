package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.ExperimentEntity
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperimentDao {
    @Insert
    suspend fun insert(experiment: ExperimentEntity)

    @Query("SELECT * FROM experiment WHERE id = :id")
    suspend fun getById(id: String): ExperimentEntity?

    @Query("SELECT * FROM experiment WHERE id = :id")
    fun observeById(id: String): Flow<ExperimentEntity?>

    @Query(
        """
    SELECT * FROM experiment
    WHERE garden_id = :gardenId
    ORDER BY created_at DESC
    """,
    )
    fun observeForGarden(gardenId: String): Flow<List<ExperimentEntity>>

    @Query(
        """
        UPDATE experiment
        SET status = :status,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: ExperimentStatus,
        updatedAt: Long,
    ): Int
}
