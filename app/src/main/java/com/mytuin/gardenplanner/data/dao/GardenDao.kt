package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GardenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenDao {

    @Insert
    suspend fun insert(garden: GardenEntity)

    @Query("SELECT * FROM garden WHERE id = :id")
    suspend fun getById(id: String): GardenEntity?

    @Query("SELECT * FROM garden ORDER BY name")
    fun observeAll(): Flow<List<GardenEntity>>

    @Query("SELECT * FROM garden WHERE id = :id")
    fun observeById(id: String): Flow<GardenEntity?>
}