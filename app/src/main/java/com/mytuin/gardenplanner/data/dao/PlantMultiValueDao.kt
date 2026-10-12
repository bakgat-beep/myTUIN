package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlantMultiValueEntity
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import kotlinx.coroutines.flow.Flow

/**
 * Insert uses REPLACE so re-adding the same (plant_id, kind,
 * value_id) triple updates notes rather than raising a constraint
 * error. Nothing references plant_multi_value rows via FK, so
 * REPLACE is safe.
 */
@Dao
interface PlantMultiValueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(row: PlantMultiValueEntity)

    @Query("SELECT * FROM plant_multi_value WHERE plant_id = :plantId")
    fun observeForPlant(plantId: String): Flow<List<PlantMultiValueEntity>>

    @Query(
        """
        SELECT * FROM plant_multi_value
        WHERE plant_id = :plantId AND kind = :kind
        """,
    )
    fun observeForPlantByKind(
        plantId: String,
        kind: PlantMultiValueKind,
    ): Flow<List<PlantMultiValueEntity>>
}
