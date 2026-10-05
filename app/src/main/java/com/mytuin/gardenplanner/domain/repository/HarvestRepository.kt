package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Harvest
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Harvest.
 *
 * insert is transactional: it writes both the Harvest and its linked
 * harvesting Activity in a single transaction (HL1).
 *
 * archive cascades through the linked Activity (HL11): Harvest has
 * no status column.
 */
interface HarvestRepository {
    fun observeHarvestsInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Harvest>>

    fun observeHarvestsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Harvest>>

    fun observeHarvestsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Harvest>>

    fun observeHarvest(id: String): Flow<Harvest?>

    suspend fun getHarvest(id: String): Harvest?

    suspend fun insert(harvest: Harvest)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )
}
