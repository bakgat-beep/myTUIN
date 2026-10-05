package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.HarvestLossEntity
import kotlinx.coroutines.flow.Flow

/**
 * HarvestLoss has no status column (HL12). Queries are not filtered
 * by any archive state.
 */
@Dao
interface HarvestLossDao {
    @Insert
    suspend fun insert(harvestLoss: HarvestLossEntity)

    @Query("SELECT * FROM harvest_loss WHERE id = :id")
    suspend fun getById(id: String): HarvestLossEntity?

    @Query("SELECT * FROM harvest_loss WHERE id = :id")
    fun observeById(id: String): Flow<HarvestLossEntity?>

    @Query(
        """
    SELECT * FROM harvest_loss
    WHERE garden_id = :gardenId
    ORDER BY date_epoch_day DESC
    """,
    )
    fun observeForGarden(gardenId: String): Flow<List<HarvestLossEntity>>

    @Query(
        """
    SELECT * FROM harvest_loss
    WHERE plant_instance_id = :plantInstanceId
    ORDER BY date_epoch_day DESC
    """,
    )
    fun observeForPlantInstance(plantInstanceId: String): Flow<List<HarvestLossEntity>>

    @Query(
        """
    SELECT * FROM harvest_loss
    WHERE growing_space_id = :growingSpaceId
    ORDER BY date_epoch_day DESC
    """,
    )
    fun observeForGrowingSpace(growingSpaceId: String): Flow<List<HarvestLossEntity>>
}
