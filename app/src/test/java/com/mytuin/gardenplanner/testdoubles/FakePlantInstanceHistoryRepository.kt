package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.PlantInstanceHistory
import com.mytuin.gardenplanner.domain.repository.PlantInstanceHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlantInstanceHistoryRepository : PlantInstanceHistoryRepository {
    private val store = MutableStateFlow<List<PlantInstanceHistory>>(emptyList())

    override fun observeHistoryFor(plantInstanceId: String): Flow<List<PlantInstanceHistory>> =
        store.map { rows -> rows.filter { it.plantInstanceId == plantInstanceId } }

    override suspend fun getHistoryFor(plantInstanceId: String): List<PlantInstanceHistory> =
        store.value.filter { it.plantInstanceId == plantInstanceId }

    override suspend fun getHistoryAsOf(
        plantInstanceId: String,
        atMillis: Long,
    ): PlantInstanceHistory? =
        store.value.firstOrNull {
            it.plantInstanceId == plantInstanceId &&
                it.validFrom <= atMillis &&
                it.validTo > atMillis
        }

    fun snapshot(): List<PlantInstanceHistory> = store.value
}
