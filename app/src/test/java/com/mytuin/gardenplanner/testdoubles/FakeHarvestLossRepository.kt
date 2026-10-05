package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.HarvestLoss
import com.mytuin.gardenplanner.domain.repository.HarvestLossRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Fake repository for HarvestLoss.
 *
 * No archive tracking: HarvestLoss has no status column (HL12).
 */
class FakeHarvestLossRepository : HarvestLossRepository {
    private val store = MutableStateFlow<List<HarvestLoss>>(emptyList())

    override fun observeHarvestLossesInGarden(gardenId: String): Flow<List<HarvestLoss>> =
        store.map { rows -> rows.filter { it.gardenId == gardenId } }

    override fun observeHarvestLossesForPlantInstance(plantInstanceId: String): Flow<List<HarvestLoss>> =
        store.map { rows -> rows.filter { it.plantInstanceId == plantInstanceId } }

    override fun observeHarvestLossesInGrowingSpace(growingSpaceId: String): Flow<List<HarvestLoss>> =
        store.map { rows -> rows.filter { it.growingSpaceId == growingSpaceId } }

    override fun observeHarvestLoss(id: String): Flow<HarvestLoss?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getHarvestLoss(id: String): HarvestLoss? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(harvestLoss: HarvestLoss) {
        store.value = store.value + harvestLoss
    }

    fun snapshot(): List<HarvestLoss> = store.value
}
