package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstanceLocation
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for PlantInstance.
 *
 * Two observations by scope: the whole garden, and one growing
 * space.
 *
 * updateLocation is the single mutation path for the three location
 * fields. Per DEC-041 it writes the prior location to the companion
 * history table in the same transaction (H2).
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

    suspend fun updateLocation(
        id: String,
        location: NewPlantInstanceLocation,
        effectiveAt: Long,
        reason: String?,
    )

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )
}
