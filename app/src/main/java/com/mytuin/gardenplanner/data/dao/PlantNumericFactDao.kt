package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantNumericFactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantNumericFactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(fact: PlantNumericFactEntity)

    @Query("SELECT * FROM plant_numeric_fact WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantNumericFactEntity>>
}
