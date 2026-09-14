package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantEntity

@Dao
interface PlantDao {

    @Insert
    suspend fun insert(plant: PlantEntity)

    @Query("SELECT * FROM plant WHERE id = :id")
    suspend fun getById(id: String): PlantEntity?
}