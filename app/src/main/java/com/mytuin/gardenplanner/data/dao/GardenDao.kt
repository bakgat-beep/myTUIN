package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenDao {
    @Insert
    suspend fun insert(garden: GardenEntity)

    @Query("SELECT * FROM garden WHERE id = :id")
    suspend fun getById(id: String): GardenEntity?

    /**
     * S2: one query serves both archive and restore.
     *
     * A231=a: archived rows are excluded unless includeArchived is
     * true. The stored value for archived is the canonical id
     * 'archived' (DEC-040).
     */
    @Query("SELECT * FROM garden WHERE status != 'archived' ORDER BY name")
    fun observeActive(): Flow<List<GardenEntity>>

    @Query("SELECT * FROM garden ORDER BY name")
    fun observeAll(): Flow<List<GardenEntity>>

    @Query("SELECT * FROM garden WHERE id = :id")
    fun observeById(id: String): Flow<GardenEntity?>

    @Query(
        """
        UPDATE garden
        SET status = :status,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: RecordStatus,
        updatedAt: Long,
    ): Int

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
        """,
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
