package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.ObservationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Observation has no status column. The "active" queries join the
 * activity table and filter on the linked Activity's status (O5).
 * The Activity is created in the same transaction as the
 * Observation, so the join is always satisfiable.
 *
 * `SELECT o.*` is used in the JOIN queries. Room maps the columns
 * of `observation` from this form.
 */
@Dao
interface ObservationDao {
    @Insert
    suspend fun insert(observation: ObservationEntity)

    @Query("SELECT * FROM observation WHERE id = :id")
    suspend fun getById(id: String): ObservationEntity?

    @Query("SELECT * FROM observation WHERE id = :id")
    fun observeById(id: String): Flow<ObservationEntity?>

    @Query(
        """
    SELECT o.* FROM observation o
    INNER JOIN activity a ON a.id = o.activity_id
    WHERE o.garden_id = :gardenId
      AND (a.status IS NULL OR a.status != 'archived')
    ORDER BY o.observed_at DESC
    """,
    )
    fun observeActiveForGarden(gardenId: String): Flow<List<ObservationEntity>>

    @Query(
        """
    SELECT * FROM observation
    WHERE garden_id = :gardenId
    ORDER BY observed_at DESC
    """,
    )
    fun observeAllForGarden(gardenId: String): Flow<List<ObservationEntity>>

    @Query(
        """
    SELECT o.* FROM observation o
    INNER JOIN activity a ON a.id = o.activity_id
    WHERE o.plant_instance_id = :plantInstanceId
      AND (a.status IS NULL OR a.status != 'archived')
    ORDER BY o.observed_at DESC
    """,
    )
    fun observeActiveForPlantInstance(plantInstanceId: String): Flow<List<ObservationEntity>>

    @Query(
        """
    SELECT * FROM observation
    WHERE plant_instance_id = :plantInstanceId
    ORDER BY observed_at DESC
    """,
    )
    fun observeAllForPlantInstance(plantInstanceId: String): Flow<List<ObservationEntity>>

    @Query(
        """
    SELECT o.* FROM observation o
    INNER JOIN activity a ON a.id = o.activity_id
    WHERE o.growing_space_id = :growingSpaceId
      AND (a.status IS NULL OR a.status != 'archived')
    ORDER BY o.observed_at DESC
    """,
    )
    fun observeActiveForGrowingSpace(growingSpaceId: String): Flow<List<ObservationEntity>>

    @Query(
        """
    SELECT * FROM observation
    WHERE growing_space_id = :growingSpaceId
    ORDER BY observed_at DESC
    """,
    )
    fun observeAllForGrowingSpace(growingSpaceId: String): Flow<List<ObservationEntity>>
}
