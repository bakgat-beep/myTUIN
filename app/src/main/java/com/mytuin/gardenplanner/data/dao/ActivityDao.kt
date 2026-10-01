package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert
    suspend fun insert(activity: ActivityEntity)

    @Query("SELECT * FROM activity WHERE id = :id")
    suspend fun getById(id: String): ActivityEntity?

    @Query("SELECT * FROM activity WHERE id = :id")
    fun observeById(id: String): Flow<ActivityEntity?>

    @Query(
        """
    SELECT * FROM activity
    WHERE garden_id = :gardenId
      AND (status IS NULL OR status != 'archived')
    ORDER BY occurred_at DESC
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<ActivityEntity>>

    @Query(
        """
    SELECT * FROM activity
    WHERE garden_id = :gardenId
    ORDER BY occurred_at DESC
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<ActivityEntity>>

    @Query(
        """
    SELECT * FROM activity
    WHERE plant_instance_id = :plantInstanceId
      AND (status IS NULL OR status != 'archived')
    ORDER BY occurred_at DESC
    """,
    )
    fun observeActiveForPlantInstance(plantInstanceId: String): Flow<List<ActivityEntity>>

    @Query(
        """
    SELECT * FROM activity
    WHERE plant_instance_id = :plantInstanceId
    ORDER BY occurred_at DESC
    """,
    )
    fun observeAllForPlantInstance(plantInstanceId: String): Flow<List<ActivityEntity>>

    @Query(
        """
    SELECT * FROM activity
    WHERE growing_space_id = :growingSpaceId
      AND (status IS NULL OR status != 'archived')
    ORDER BY occurred_at DESC
    """,
    )
    fun observeActiveForGrowingSpace(growingSpaceId: String): Flow<List<ActivityEntity>>

    @Query(
        """
    SELECT * FROM activity
    WHERE growing_space_id = :growingSpaceId
    ORDER BY occurred_at DESC
    """,
    )
    fun observeAllForGrowingSpace(growingSpaceId: String): Flow<List<ActivityEntity>>

    @Query(
        """
        UPDATE activity
        SET status = :status
        WHERE id = :id
        """,
    )
    suspend fun updateStatus(
        id: String,
        status: RecordStatus,
    ): Int
}
