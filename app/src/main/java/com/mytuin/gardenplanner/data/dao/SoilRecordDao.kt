package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.SoilRecordEntity
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SoilRecordDao {
    @Insert
    suspend fun insert(soilRecord: SoilRecordEntity)

    @Query("SELECT * FROM soil_record WHERE id = :id")
    suspend fun getById(id: String): SoilRecordEntity?

    @Query("SELECT * FROM soil_record WHERE id = :id")
    fun observeById(id: String): Flow<SoilRecordEntity?>

    @Query(
        """
    SELECT * FROM soil_record
    WHERE garden_id = :gardenId
      AND status != 'archived'
    ORDER BY name
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<SoilRecordEntity>>

    @Query(
        """
    SELECT * FROM soil_record
    WHERE garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<SoilRecordEntity>>

    @Query(
        """
        UPDATE soil_record
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
