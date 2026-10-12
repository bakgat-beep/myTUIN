package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantTemporalFactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantTemporalFactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(fact: PlantTemporalFactEntity)

    @Query("SELECT * FROM plant_temporal_fact WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantTemporalFactEntity>>
}
