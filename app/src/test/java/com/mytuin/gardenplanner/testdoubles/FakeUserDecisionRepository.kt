package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.recommendation.UserDecision
import com.mytuin.gardenplanner.domain.repository.UserDecisionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeUserDecisionRepository : UserDecisionRepository {
    private val store = MutableStateFlow<List<UserDecision>>(emptyList())

    override fun observeUserDecisionsInGarden(gardenId: String): Flow<List<UserDecision>> =
        store.map { rows -> rows.filter { it.gardenId == gardenId } }

    override fun observeUserDecisionsForRecommendation(recommendationId: String): Flow<List<UserDecision>> =
        store.map { rows -> rows.filter { it.recommendationId == recommendationId } }

    override fun observeUserDecisionsForPlan(planId: String): Flow<List<UserDecision>> =
        store.map { rows -> rows.filter { it.planId == planId } }

    override fun observeUserDecision(id: String): Flow<UserDecision?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getUserDecision(id: String): UserDecision? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(decision: UserDecision) {
        store.value = store.value + decision
    }

    fun snapshot(): List<UserDecision> = store.value
}
