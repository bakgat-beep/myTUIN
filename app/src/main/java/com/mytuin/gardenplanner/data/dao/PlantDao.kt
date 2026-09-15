package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Insert
    suspend fun insert(plant: PlantEntity)

    @Query("SELECT * FROM plant WHERE id = :id")
    suspend fun getById(id: String): PlantEntity?

    @Query("SELECT * FROM plant ORDER BY canonical_name")
    fun observeAll(): Flow<List<PlantEntity>>

    @Query("SELECT * FROM plant WHERE id = :id")
    fun observeById(id: String): Flow<PlantEntity?>
}