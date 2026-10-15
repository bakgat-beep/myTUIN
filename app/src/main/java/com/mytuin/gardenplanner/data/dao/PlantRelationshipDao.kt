package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantRelationshipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantRelationshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(relationship: PlantRelationshipEntity)

    @Query("SELECT * FROM plant_relationship WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantRelationshipEntity>>
}
