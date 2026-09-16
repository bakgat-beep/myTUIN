package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
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

    /**
     * Replaces the seven location fields on a Garden and updates
     * updated_at. Returns the number of rows affected, so the
     * repository can throw NotFoundError when the id does not match.
     *
     * The hemisphere parameter is TypeConverted via
     * VocabularyConverters.
     */
    @Query(
        """
        UPDATE garden
        SET country_code = :countryCode,
            region = :region,
            locality = :locality,
            latitude = :latitude,
            longitude = :longitude,
            timezone = :timezone,
            hemisphere = :hemisphere,
            updated_at = :updatedAt
        WHERE id = :id
        """
    )
    suspend fun updateLocation(
        id: String,
        countryCode: String?,
        region: String?,
        locality: String?,
        latitude: Double?,
        longitude: Double?,
        timezone: String?,
        hemisphere: Hemisphere,
        updatedAt: Long,
    ): Int
}