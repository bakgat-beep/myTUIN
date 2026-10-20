package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.RecommendationDao
import com.mytuin.gardenplanner.data.dao.RecommendationEvidenceDao
import com.mytuin.gardenplanner.data.dao.RecommendationFactorDao
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.recommendation.Recommendation
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationEvidence
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationFactor
import com.mytuin.gardenplanner.domain.repository.RecommendationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * RecommendationRepositoryImpl.
 *
 * Pre-checks the direct FKs (garden for Recommendation,
 * recommendation for Factor and Evidence). Does not pre-check the
 * polymorphic subject_id or source_id (R2, R3): valid references are
 * the engine's responsibility.
 */
class RecommendationRepositoryImpl
    @Inject
    constructor(
        private val recommendationDao: RecommendationDao,
        private val recommendationFactorDao: RecommendationFactorDao,
        private val recommendationEvidenceDao: RecommendationEvidenceDao,
        private val gardenDao: GardenDao,
    ) : RecommendationRepository {
        override fun observeRecommendationsInGarden(gardenId: String): Flow<List<Recommendation>> =
            recommendationDao
                .observeForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeRecommendation(id: String): Flow<Recommendation?> = recommendationDao.observeById(id).map { it?.toDomain() }

        override suspend fun getRecommendation(id: String): Recommendation? = recommendationDao.getById(id)?.toDomain()

        override suspend fun insertRecommendation(recommendation: Recommendation) {
            requireGardenExists(recommendation.gardenId)
            try {
                recommendationDao.insert(recommendation.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "recommendation",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override fun observeFactorsForRecommendation(recommendationId: String): Flow<List<RecommendationFactor>> =
            recommendationFactorDao
                .observeForRecommendation(recommendationId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun insertFactor(factor: RecommendationFactor) {
            requireRecommendationExists(factor.recommendationId)
            try {
                recommendationFactorDao.insert(factor.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "recommendation_id",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override fun observeEvidenceForRecommendation(recommendationId: String): Flow<List<RecommendationEvidence>> =
            recommendationEvidenceDao
                .observeForRecommendation(recommendationId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun insertEvidence(evidence: RecommendationEvidence) {
            requireRecommendationExists(evidence.recommendationId)
            try {
                recommendationEvidenceDao.insert(evidence.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "recommendation_id",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        private suspend fun requireGardenExists(gardenId: String) {
            gardenDao.getById(gardenId)
                ?: throw ValidationError(
                    field = "garden_id",
                    reason = "Garden does not exist: $gardenId",
                )
        }

        private suspend fun requireRecommendationExists(recommendationId: String) {
            recommendationDao.getById(recommendationId)
                ?: throw ValidationError(
                    field = "recommendation_id",
                    reason = "Recommendation does not exist: $recommendationId",
                )
        }
    }
