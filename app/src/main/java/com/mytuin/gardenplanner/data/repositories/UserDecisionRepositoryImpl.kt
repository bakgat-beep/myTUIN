package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.PlanDao
import com.mytuin.gardenplanner.data.dao.RecommendationDao
import com.mytuin.gardenplanner.data.dao.UserDecisionDao
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.recommendation.UserDecision
import com.mytuin.gardenplanner.domain.repository.UserDecisionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDecisionRepositoryImpl
    @Inject
    constructor(
        private val userDecisionDao: UserDecisionDao,
        private val gardenDao: GardenDao,
        private val recommendationDao: RecommendationDao,
        private val planDao: PlanDao,
    ) : UserDecisionRepository {
        override fun observeUserDecisionsInGarden(gardenId: String): Flow<List<UserDecision>> =
            userDecisionDao
                .observeForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeUserDecisionsForRecommendation(recommendationId: String): Flow<List<UserDecision>> =
            userDecisionDao
                .observeForRecommendation(recommendationId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeUserDecisionsForPlan(planId: String): Flow<List<UserDecision>> =
            userDecisionDao
                .observeForPlan(planId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeUserDecision(id: String): Flow<UserDecision?> = userDecisionDao.observeById(id).map { it?.toDomain() }

        override suspend fun getUserDecision(id: String): UserDecision? = userDecisionDao.getById(id)?.toDomain()

        override suspend fun insert(decision: UserDecision) {
            requireGardenExists(decision.gardenId)
            decision.recommendationId?.let { requireRecommendationExists(it) }
            decision.planId?.let { requirePlanExists(it) }
            try {
                userDecisionDao.insert(decision.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "user_decision",
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

        private suspend fun requirePlanExists(planId: String) {
            planDao.getById(planId)
                ?: throw ValidationError(
                    field = "plan_id",
                    reason = "Plan does not exist: $planId",
                )
        }
    }
