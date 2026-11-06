package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.repository.SpatialObjectHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSpatialObjectHistoryRepository : SpatialObjectHistoryRepository {
    private val store = MutableStateFlow<List<SpatialObjectHistory>>(emptyList())

    override fun observeHistoryForObject(spatialObjectId: String): Flow<List<SpatialObjectHistory>> =
        store.map { rows ->
            rows.filter { it.spatialObjectId == spatialObjectId }.sortedByDescending { it.validFrom }
        }

    override suspend fun getHistoryForObject(spatialObjectId: String): List<SpatialObjectHistory> =
        store.value.filter { it.spatialObjectId == spatialObjectId }

    override suspend fun getHistoryAsOf(
        spatialObjectId: String,
        atMillis: Long,
    ): SpatialObjectHistory? =
        store.value.firstOrNull {
            it.spatialObjectId == spatialObjectId &&
                it.validFrom <= atMillis &&
                it.validTo > atMillis
        }

    /** Visible-for-tests seeding hook. */
    fun seed(rows: List<SpatialObjectHistory>) {
        store.value = rows
    }

    fun snapshot(): List<SpatialObjectHistory> = store.value
}
