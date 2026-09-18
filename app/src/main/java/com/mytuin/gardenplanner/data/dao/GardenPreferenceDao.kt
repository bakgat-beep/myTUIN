package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GardenPlantPreferenceEntity
import com.mytuin.gardenplanner.data.entities.GardenPreferenceEntity
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import kotlinx.coroutines.flow.Flow

/**
 * Insert uses REPLACE so setting the same key or plant-preference
 * twice overwrites rather than failing on the composite primary key.
 * REPLACE is safe here: nothing references preference rows via FK.
 */
@Dao
interface GardenPreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(preference: GardenPreferenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPlantPreference(preference: GardenPlantPreferenceEntity)

    @Query("SELECT * FROM garden_preference WHERE garden_id = :gardenId")
    suspend fun getPreferences(gardenId: String): List<GardenPreferenceEntity>

    @Query("SELECT * FROM garden_preference WHERE garden_id = :gardenId")
    fun observePreferences(gardenId: String): Flow<List<GardenPreferenceEntity>>

    @Query(
        "SELECT * FROM garden_preference " +
                "WHERE garden_id = :gardenId AND preference_key = :key"
    )
    suspend fun getPreference(
        gardenId: String,
        key: GardenPreferenceKey,
    ): GardenPreferenceEntity?

    @Query(
        "DELETE FROM garden_preference " +
                "WHERE garden_id = :gardenId AND preference_key = :key"
    )
    suspend fun deletePreference(gardenId: String, key: GardenPreferenceKey)

    @Query(
        "SELECT * FROM garden_plant_preference " +
                "WHERE garden_id = :gardenId AND kind = :kind"
    )
    suspend fun getPlantPreferences(
        gardenId: String,
        kind: GardenPlantPreferenceKind,
    ): List<GardenPlantPreferenceEntity>

    @Query(
        "SELECT * FROM garden_plant_preference " +
                "WHERE garden_id = :gardenId AND kind = :kind"
    )
    fun observePlantPreferences(
        gardenId: String,
        kind: GardenPlantPreferenceKind,
    ): Flow<List<GardenPlantPreferenceEntity>>

    @Query(
        "DELETE FROM garden_plant_preference " +
                "WHERE garden_id = :gardenId AND plant_id = :plantId AND kind = :kind"
    )
    suspend fun deletePlantPreference(
        gardenId: String,
        plantId: String,
        kind: GardenPlantPreferenceKind,
    )
}