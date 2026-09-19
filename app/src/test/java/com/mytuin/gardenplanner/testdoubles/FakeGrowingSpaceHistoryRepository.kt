package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeGrowingSpaceHistoryRepository : GrowingSpaceHistoryRepository {
    private val store = MutableStateFlow<List<GrowingSpaceHistory>>(emptyList())

    override fun observeHistoryForSpace(growingSpaceId: String): Flow<List<GrowingSpaceHistory>> =
        store.map { rows -> rows.filter { it.growingSpaceId == growingSpaceId } }

    override suspend fun getHistoryForSpace(growingSpaceId: String): List<GrowingSpaceHistory> =
        store.value.filter { it.growingSpaceId == growingSpaceId }

    override suspend fun getHistoryAsOf(
        growingSpaceId: String,
        atMillis: Long,
    ): GrowingSpaceHistory? =
        store.value.firstOrNull {
            it.growingSpaceId == growingSpaceId &&
                it.validFrom <= atMillis &&
                it.validTo > atMillis
        }

    fun snapshot(): List<GrowingSpaceHistory> = store.value
}
