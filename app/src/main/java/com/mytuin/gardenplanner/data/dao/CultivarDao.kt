package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.CultivarEntity

@Dao
interface CultivarDao {

    @Insert
    suspend fun insert(cultivar: CultivarEntity)

    @Query("SELECT * FROM cultivar WHERE plant_id = :plantId")
    suspend fun getForPlant(plantId: String): List<CultivarEntity>
}