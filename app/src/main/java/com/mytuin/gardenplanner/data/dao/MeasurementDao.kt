package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.MeasurementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {
    @Insert
    suspend fun insert(measurement: MeasurementEntity)

    @Query("SELECT * FROM measurement WHERE id = :id")
    suspend fun getById(id: String): MeasurementEntity?

    @Query("SELECT * FROM measurement WHERE id = :id")
    fun observeById(id: String): Flow<MeasurementEntity?>

    @Query(
        """
    SELECT * FROM measurement
    WHERE garden_id = :gardenId
    ORDER BY measured_at_epoch_day DESC
    """,
    )
    fun observeForGarden(gardenId: String): Flow<List<MeasurementEntity>>

    @Query(
        """
    SELECT * FROM measurement
    WHERE plant_instance_id = :plantInstanceId
    ORDER BY measured_at_epoch_day DESC
    """,
    )
    fun observeForPlantInstance(plantInstanceId: String): Flow<List<MeasurementEntity>>

    @Query(
        """
    SELECT * FROM measurement
    WHERE growing_space_id = :growingSpaceId
    ORDER BY measured_at_epoch_day DESC
    """,
    )
    fun observeForGrowingSpace(growingSpaceId: String): Flow<List<MeasurementEntity>>
}
