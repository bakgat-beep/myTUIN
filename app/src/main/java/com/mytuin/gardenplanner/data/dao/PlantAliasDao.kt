package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantAliasDao {

    @Insert
    suspend fun insert(alias: PlantAliasEntity)

    @Query("SELECT * FROM plant_alias WHERE plant_id = :plantId")
    suspend fun getForPlant(plantId: String): List<PlantAliasEntity>

    @Query("SELECT * FROM plant_alias WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantAliasEntity>>
}