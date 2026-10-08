package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.PlanTargetEntity
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import kotlinx.coroutines.flow.Flow

/**
 * PlanTarget has no stable id; its identity is the composite primary
 * key (plan_id, target_type, target_id). Insert uses REPLACE so
 * re-adding the same target updates notes rather than raising a
 * constraint error. Because REPLACE deletes-then-inserts, the
 * repository's addTarget reads the prior completed_at first and
 * includes it in the new row, preserving the completion state.
 */
@Dao
interface PlanTargetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(target: PlanTargetEntity)

    @Query("SELECT * FROM plan_target WHERE plan_id = :planId")
    fun observeForPlan(planId: String): Flow<List<PlanTargetEntity>>

    @Query(
        """
        SELECT * FROM plan_target
        WHERE plan_id = :planId
          AND target_type = :targetType
          AND target_id = :targetId
        """,
    )
    suspend fun get(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
    ): PlanTargetEntity?

    @Query(
        """
        UPDATE plan_target
        SET completed_at = :completedAt
        WHERE plan_id = :planId
          AND target_type = :targetType
          AND target_id = :targetId
        """,
    )
    suspend fun markCompleted(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
        completedAt: Long,
    ): Int

    @Query(
        """
        DELETE FROM plan_target
        WHERE plan_id = :planId
          AND target_type = :targetType
          AND target_id = :targetId
        """,
    )
    suspend fun delete(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
    )
}
