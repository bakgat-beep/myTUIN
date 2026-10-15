package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantProblemRelationshipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantProblemRelationshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(relationship: PlantProblemRelationshipEntity)

    @Query("SELECT * FROM plant_problem_relationship WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantProblemRelationshipEntity>>
}
