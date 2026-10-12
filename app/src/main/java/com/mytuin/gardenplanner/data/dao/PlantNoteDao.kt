package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: PlantNoteEntity)

    @Query("SELECT * FROM plant_note WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantNoteEntity>>
}
