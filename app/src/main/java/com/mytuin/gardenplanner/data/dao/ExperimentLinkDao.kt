package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.ExperimentLinkEntity
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperimentLinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(link: ExperimentLinkEntity)

    @Query("SELECT * FROM experiment_link WHERE experiment_id = :experimentId")
    fun observeForExperiment(experimentId: String): Flow<List<ExperimentLinkEntity>>

    @Query(
        """
        DELETE FROM experiment_link
        WHERE experiment_id = :experimentId
          AND linked_type = :linkedType
          AND linked_id = :linkedId
        """,
    )
    suspend fun delete(
        experimentId: String,
        linkedType: ExperimentLinkType,
        linkedId: String,
    )
}
