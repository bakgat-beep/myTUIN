package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SpatialObjectDao {
    @Insert
    suspend fun insert(spatialObject: SpatialObjectEntity)

    @Query("SELECT * FROM spatial_object WHERE id = :id")
    suspend fun getById(id: String): SpatialObjectEntity?

    @Query(
        """
    SELECT * FROM spatial_object
    WHERE garden_id = :gardenId AND status != 'archived'
    ORDER BY name
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<SpatialObjectEntity>>

    @Query(
        """
    SELECT * FROM spatial_object
    WHERE garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<SpatialObjectEntity>>

    @Query("SELECT * FROM spatial_object WHERE id = :id")
    fun observeById(id: String): Flow<SpatialObjectEntity?>

    @Query(
        """
        UPDATE spatial_object
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
}
