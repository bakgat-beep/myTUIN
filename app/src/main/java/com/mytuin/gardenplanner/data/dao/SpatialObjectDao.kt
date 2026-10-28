package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.SpatialObjectEntity
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
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

    /**
     * Updates the mutable scalar fields only (Step 9e).
     *
     * Geometry is deliberately excluded: geometry changes on
     * SpatialObject would require a companion history table per
     * DEC-041, which 9i owns. status is excluded: status changes
     * go through updateStatus (archive/restore).
     */
    @Query(
        """
        UPDATE spatial_object
        SET object_type = :objectType,
            name = :name,
            description = :description,
            area_id = :areaId,
            notes = :notes,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateFields(
        id: String,
        objectType: InfrastructureType,
        name: String?,
        description: String?,
        areaId: String?,
        notes: String?,
        updatedAt: Long,
    ): Int
}
