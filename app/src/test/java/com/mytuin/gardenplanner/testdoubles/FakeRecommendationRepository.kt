package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.recommendation.Recommendation
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationEvidence
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationFactor
import com.mytuin.gardenplanner.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeRecommendationRepository : RecommendationRepository {
    private val recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    private val factors = MutableStateFlow<List<RecommendationFactor>>(emptyList())
    private val evidence = MutableStateFlow<List<RecommendationEvidence>>(emptyList())

    override fun observeRecommendationsInGarden(gardenId: String): Flow<List<Recommendation>> =
        recommendations.map { rows -> rows.filter { it.gardenId == gardenId } }

    override fun observeRecommendation(id: String): Flow<Recommendation?> = recommendations.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getRecommendation(id: String): Recommendation? = recommendations.value.firstOrNull { it.id == id }

    override suspend fun insertRecommendation(recommendation: Recommendation) {
        recommendations.value = recommendations.value + recommendation
    }

    override fun observeFactorsForRecommendation(recommendationId: String): Flow<List<RecommendationFactor>> =
        factors.map { rows -> rows.filter { it.recommendationId == recommendationId } }

    override suspend fun insertFactor(factor: RecommendationFactor) {
        factors.value = factors.value + factor
    }

    override fun observeEvidenceForRecommendation(recommendationId: String): Flow<List<RecommendationEvidence>> =
        evidence.map { rows -> rows.filter { it.recommendationId == recommendationId } }

    override suspend fun insertEvidence(evidence: RecommendationEvidence) {
        this.evidence.value = this.evidence.value + evidence
    }

    fun recommendationsSnapshot(): List<Recommendation> = recommendations.value

    fun factorsSnapshot(): List<RecommendationFactor> = factors.value

    fun evidenceSnapshot(): List<RecommendationEvidence> = evidence.value
}
