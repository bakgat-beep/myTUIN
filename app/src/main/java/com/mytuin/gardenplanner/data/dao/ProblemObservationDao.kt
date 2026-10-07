package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.ProblemObservationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Insert uses REPLACE so re-linking the same pair overwrites the
 * prior evidence direction and notes rather than failing on the
 * composite primary key. Nothing references problem_observation
 * rows via FK, so REPLACE is safe.
 */
@Dao
interface ProblemObservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(link: ProblemObservationEntity)

    @Query("SELECT * FROM problem_observation WHERE problem_id = :problemId")
    fun observeForProblem(problemId: String): Flow<List<ProblemObservationEntity>>

    @Query("SELECT * FROM problem_observation WHERE observation_id = :observationId")
    fun observeForObservation(observationId: String): Flow<List<ProblemObservationEntity>>

    @Query(
        "DELETE FROM problem_observation " +
            "WHERE problem_id = :problemId AND observation_id = :observationId",
    )
    suspend fun delete(
        problemId: String,
        observationId: String,
    )

    @Query(
        "SELECT EXISTS(SELECT 1 FROM problem_observation " +
            "WHERE problem_id = :problemId AND observation_id = :observationId)",
    )
    suspend fun exists(
        problemId: String,
        observationId: String,
    ): Boolean
}
