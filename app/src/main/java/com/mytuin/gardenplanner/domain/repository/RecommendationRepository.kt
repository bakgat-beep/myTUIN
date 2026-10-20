package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.recommendation.Recommendation
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationEvidence
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationFactor
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Recommendation, RecommendationFactor and
 * RecommendationEvidence.
 *
 * Three entities grouped because factors and evidence only exist in
 * relation to a recommendation (R8). Recommendation retirement is
 * `status = expired` (R7); no archive/restore methods.
 *
 * R9: insert methods only. No Create* use case in this step; the
 * engine that constructs recommendations is Phase 5 work. Insert
 * takes a fully-formed domain object, matching the pattern of
 * GardenRepository.insert(garden).
 *
 * subject_id and source_id are polymorphic and carry no FK (R2, R3).
 * The repository does not pre-check their existence: valid
 * references are the engine's responsibility.
 */
interface RecommendationRepository {
    fun observeRecommendationsInGarden(gardenId: String): Flow<List<Recommendation>>

    fun observeRecommendation(id: String): Flow<Recommendation?>

    suspend fun getRecommendation(id: String): Recommendation?

    suspend fun insertRecommendation(recommendation: Recommendation)

    fun observeFactorsForRecommendation(recommendationId: String): Flow<List<RecommendationFactor>>

    suspend fun insertFactor(factor: RecommendationFactor)

    fun observeEvidenceForRecommendation(recommendationId: String): Flow<List<RecommendationEvidence>>

    suspend fun insertEvidence(evidence: RecommendationEvidence)
}
