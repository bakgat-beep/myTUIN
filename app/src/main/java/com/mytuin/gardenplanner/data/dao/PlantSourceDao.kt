package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantSourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantSourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(link: PlantSourceEntity)

    @Query("SELECT * FROM plant_source WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantSourceEntity>>
}
