package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.RecommendationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationDao {
    @Insert
    suspend fun insert(recommendation: RecommendationEntity)

    @Query("SELECT * FROM recommendation WHERE id = :id")
    suspend fun getById(id: String): RecommendationEntity?

    @Query("SELECT * FROM recommendation WHERE id = :id")
    fun observeById(id: String): Flow<RecommendationEntity?>

    @Query(
        """
    SELECT * FROM recommendation
    WHERE garden_id = :gardenId
    ORDER BY created_at DESC
    """,
    )
    fun observeForGarden(gardenId: String): Flow<List<RecommendationEntity>>
}
