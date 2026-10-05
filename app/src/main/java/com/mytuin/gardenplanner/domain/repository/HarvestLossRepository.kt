package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.HarvestLoss
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for HarvestLoss.
 *
 * No archive or restore: HarvestLoss has no status column (HL12).
 * The queries take no includeArchived parameter for the same reason.
 * Same shape as MeasurementRepository.
 */
interface HarvestLossRepository {
    fun observeHarvestLossesInGarden(gardenId: String): Flow<List<HarvestLoss>>

    fun observeHarvestLossesForPlantInstance(plantInstanceId: String): Flow<List<HarvestLoss>>

    fun observeHarvestLossesInGrowingSpace(growingSpaceId: String): Flow<List<HarvestLoss>>

    fun observeHarvestLoss(id: String): Flow<HarvestLoss?>

    suspend fun getHarvestLoss(id: String): HarvestLoss?

    suspend fun insert(harvestLoss: HarvestLoss)
}
