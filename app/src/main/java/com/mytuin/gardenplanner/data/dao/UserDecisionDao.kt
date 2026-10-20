package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.UserDecisionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDecisionDao {
    @Insert
    suspend fun insert(decision: UserDecisionEntity)

    @Query("SELECT * FROM user_decision WHERE id = :id")
    suspend fun getById(id: String): UserDecisionEntity?

    @Query("SELECT * FROM user_decision WHERE id = :id")
    fun observeById(id: String): Flow<UserDecisionEntity?>

    @Query(
        """
    SELECT * FROM user_decision
    WHERE garden_id = :gardenId
    ORDER BY decided_at DESC
    """,
    )
    fun observeForGarden(gardenId: String): Flow<List<UserDecisionEntity>>

    @Query(
        """
    SELECT * FROM user_decision
    WHERE recommendation_id = :recommendationId
    ORDER BY decided_at DESC
    """,
    )
    fun observeForRecommendation(recommendationId: String): Flow<List<UserDecisionEntity>>

    @Query(
        """
    SELECT * FROM user_decision
    WHERE plan_id = :planId
    ORDER BY decided_at DESC
    """,
    )
    fun observeForPlan(planId: String): Flow<List<UserDecisionEntity>>
}
