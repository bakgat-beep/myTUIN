package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.RotationGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RotationGroupDao {
    @Insert
    suspend fun insert(group: RotationGroupEntity)

    @Query("SELECT * FROM rotation_group WHERE id = :id")
    suspend fun getById(id: String): RotationGroupEntity?

    @Query("SELECT * FROM rotation_group WHERE id = :id")
    fun observeById(id: String): Flow<RotationGroupEntity?>

    @Query(
        """
    SELECT * FROM rotation_group
    WHERE garden_id IS NULL
    ORDER BY name
    """,
    )
    fun observeSystemGroups(): Flow<List<RotationGroupEntity>>

    @Query(
        """
    SELECT * FROM rotation_group
    WHERE garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeGroupsForGarden(gardenId: String): Flow<List<RotationGroupEntity>>

    @Query(
        """
    SELECT * FROM rotation_group
    WHERE garden_id IS NULL OR garden_id = :gardenId
    ORDER BY name
    """,
    )
    fun observeAvailableForGarden(gardenId: String): Flow<List<RotationGroupEntity>>
}
