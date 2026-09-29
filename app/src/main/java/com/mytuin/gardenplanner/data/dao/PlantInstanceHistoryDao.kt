package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantInstanceHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantInstanceHistoryDao {
    @Insert
    suspend fun insert(history: PlantInstanceHistoryEntity)

    @Query(
        """
        SELECT * FROM plant_instance_history
        WHERE plant_instance_id = :plantInstanceId
        ORDER BY valid_from DESC
        """,
    )
    fun observeForInstance(plantInstanceId: String): Flow<List<PlantInstanceHistoryEntity>>

    @Query(
        """
        SELECT * FROM plant_instance_history
        WHERE plant_instance_id = :plantInstanceId
        ORDER BY valid_from DESC
        """,
    )
    suspend fun getForInstance(plantInstanceId: String): List<PlantInstanceHistoryEntity>

    @Query(
        """
        SELECT * FROM plant_instance_history
        WHERE plant_instance_id = :plantInstanceId
        ORDER BY valid_from DESC
        LIMIT 1
        """,
    )
    suspend fun getLatestForInstance(plantInstanceId: String): PlantInstanceHistoryEntity?

    @Query(
        """
        SELECT * FROM plant_instance_history
        WHERE plant_instance_id = :plantInstanceId
          AND valid_from <= :atMillis
          AND valid_to > :atMillis
        LIMIT 1
        """,
    )
    suspend fun getAsOf(
        plantInstanceId: String,
        atMillis: Long,
    ): PlantInstanceHistoryEntity?
}
