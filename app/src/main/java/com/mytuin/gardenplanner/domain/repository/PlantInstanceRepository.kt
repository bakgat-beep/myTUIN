package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for PlantInstance.
 *
 * Two observations by scope: the whole garden, and one growing
 * space. The second is what Phase 3's My Plants screen will need;
 * adding it now costs no schema and no migration.
 *
 * No update method yet. Changes to location or lifecycle arrive
 * with the PlantInstanceHistory table in step 2b and with the
 * lifecycle-activation step 2c.
 */
interface PlantInstanceRepository {
    fun observePlantInstancesInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<PlantInstance>>

    fun observePlantInstancesInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<PlantInstance>>

    fun observePlantInstance(id: String): Flow<PlantInstance?>

    suspend fun getPlantInstance(id: String): PlantInstance?

    suspend fun insert(plantInstance: PlantInstance)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )
}
