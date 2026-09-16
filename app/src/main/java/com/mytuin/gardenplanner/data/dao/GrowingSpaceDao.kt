package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowingSpaceDao {

    @Insert
    suspend fun insert(growingSpace: GrowingSpaceEntity)

    @Query("SELECT * FROM growing_space WHERE id = :id")
    suspend fun getById(id: String): GrowingSpaceEntity?

    @Query("SELECT * FROM growing_space WHERE garden_id = :gardenId ORDER BY name")
    fun observeForGarden(gardenId: String): Flow<List<GrowingSpaceEntity>>

    @Query("SELECT * FROM growing_space WHERE id = :id")
    fun observeById(id: String): Flow<GrowingSpaceEntity?>
}