package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowingSpaceDao {
    @Insert
    suspend fun insert(growingSpace: GrowingSpaceEntity)

    @Query("SELECT * FROM growing_space WHERE id = :id")
    suspend fun getById(id: String): GrowingSpaceEntity?

    @Query(
        """
    SELECT * FROM growing_space
    WHERE garden_id = :gardenId AND status != 'archived'
    ORDER BY name
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<GrowingSpaceEntity>>

    @Query(
        """
    SELECT * FROM growing_space
    WHERE garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<GrowingSpaceEntity>>

    @Query("SELECT * FROM growing_space WHERE id = :id")
    fun observeById(id: String): Flow<GrowingSpaceEntity?>

    @Query(
        """
        UPDATE growing_space
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
        UPDATE growing_space
        SET geometry_type = :geometryType,
            geometry_data = :geometryData,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateGeometry(
        id: String,
        geometryType: GeometryType?,
        geometryData: String?,
        updatedAt: Long,
    )

    /**
     * Updates the mutable scalar fields only (Step 9d, D6 (a)).
     * Geometry, status, garden_id and created_at are deliberately
     * excluded from this query: geometry is a state record with
     * history (DEC-041) and must go through updateGeometry; status
     * changes go through archive/restore; garden_id and created_at
     * are immutable.
     */
    @Query(
        """
        UPDATE growing_space
        SET name = :name,
            space_type = :spaceType,
            description = :description,
            notes = :notes,
            length = :length,
            width = :width,
            height = :height,
            diameter = :diameter,
            area = :area,
            volume = :volume,
            updated_at = :updatedAt
        WHERE id = :id
        """,
    )
    suspend fun updateFields(
        id: String,
        name: String,
        spaceType: GrowingSpaceType,
        description: String?,
        notes: String?,
        length: Double?,
        width: Double?,
        height: Double?,
        diameter: Double?,
        area: Double?,
        volume: Double?,
        updatedAt: Long,
    ): Int
}
