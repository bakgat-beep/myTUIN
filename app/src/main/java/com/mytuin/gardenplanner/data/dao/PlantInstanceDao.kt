package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantInstanceDao {
    @Insert
    suspend fun insert(plantInstance: PlantInstanceEntity)

    @Query("SELECT * FROM plant_instance WHERE id = :id")
    suspend fun getById(id: String): PlantInstanceEntity?

    @Query("SELECT * FROM plant_instance WHERE id = :id")
    fun observeById(id: String): Flow<PlantInstanceEntity?>

    @Query(
        """
    SELECT * FROM plant_instance
    WHERE garden_id = :gardenId AND status != 'archived'
    ORDER BY name
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<PlantInstanceEntity>>

    @Query(
        """
    SELECT * FROM plant_instance
    WHERE garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<PlantInstanceEntity>>

    @Query(
        """
    SELECT * FROM plant_instance
    WHERE growing_space_id = :growingSpaceId AND status != 'archived'
    ORDER BY name
    """,
    )
    fun observeActiveForGrowingSpace(growingSpaceId: String): Flow<List<PlantInstanceEntity>>

    @Query(
        """
    SELECT * FROM plant_instance
    WHERE growing_space_id = :growingSpaceId
    ORDER BY name
    """,
    )
    fun observeAllForGrowingSpace(growingSpaceId: String): Flow<List<PlantInstanceEntity>>

    @Query(
        """
        UPDATE plant_instance
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
