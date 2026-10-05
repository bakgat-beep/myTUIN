package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.HarvestEntity
import kotlinx.coroutines.flow.Flow

/**
 * Harvest has no status column. The "active" queries join the
 * activity table and filter on the linked Activity's status (HL11).
 */
@Dao
interface HarvestDao {
    @Insert
    suspend fun insert(harvest: HarvestEntity)

    @Query("SELECT * FROM harvest WHERE id = :id")
    suspend fun getById(id: String): HarvestEntity?

    @Query("SELECT * FROM harvest WHERE id = :id")
    fun observeById(id: String): Flow<HarvestEntity?>

    @Query(
        """
    SELECT h.* FROM harvest h
    INNER JOIN activity a ON a.id = h.activity_id
    WHERE h.garden_id = :gardenId
      AND (a.status IS NULL OR a.status != 'archived')
    ORDER BY h.date_epoch_day DESC
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<HarvestEntity>>

    @Query(
        """
    SELECT * FROM harvest
    WHERE garden_id = :gardenId
    ORDER BY date_epoch_day DESC
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<HarvestEntity>>

    @Query(
        """
    SELECT h.* FROM harvest h
    INNER JOIN activity a ON a.id = h.activity_id
    WHERE h.plant_instance_id = :plantInstanceId
      AND (a.status IS NULL OR a.status != 'archived')
    ORDER BY h.date_epoch_day DESC
    """,
    )
    fun observeActiveForPlantInstance(plantInstanceId: String): Flow<List<HarvestEntity>>

    @Query(
        """
    SELECT * FROM harvest
    WHERE plant_instance_id = :plantInstanceId
    ORDER BY date_epoch_day DESC
    """,
    )
    fun observeAllForPlantInstance(plantInstanceId: String): Flow<List<HarvestEntity>>

    @Query(
        """
    SELECT h.* FROM harvest h
    INNER JOIN activity a ON a.id = h.activity_id
    WHERE h.growing_space_id = :growingSpaceId
      AND (a.status IS NULL OR a.status != 'archived')
    ORDER BY h.date_epoch_day DESC
    """,
    )
    fun observeActiveForGrowingSpace(growingSpaceId: String): Flow<List<HarvestEntity>>

    @Query(
        """
    SELECT * FROM harvest
    WHERE growing_space_id = :growingSpaceId
    ORDER BY date_epoch_day DESC
    """,
    )
    fun observeAllForGrowingSpace(growingSpaceId: String): Flow<List<HarvestEntity>>
}
