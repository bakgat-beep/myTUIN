package com.mytuin.gardenplanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mytuin.gardenplanner.data.entities.RecommendationEvidenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecommendationEvidenceDao {
    @Insert
    suspend fun insert(evidence: RecommendationEvidenceEntity)

    @Query("SELECT * FROM recommendation_evidence WHERE recommendation_id = :recommendationId")
    fun observeForRecommendation(recommendationId: String): Flow<List<RecommendationEvidenceEntity>>
}
