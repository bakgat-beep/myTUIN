package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.RecommendationFactorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationFactorDao {
    @Insert
    suspend fun insert(factor: RecommendationFactorEntity)

    @Query("SELECT * FROM recommendation_factor WHERE recommendation_id = :recommendationId")
    fun observeForRecommendation(recommendationId: String): Flow<List<RecommendationFactorEntity>>
}
