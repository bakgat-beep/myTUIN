package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for GrowingSpace.
 *
 * V1_TECHNICAL_ARCHITECTURE §9. Reads return Flow or suspend (A24);
 * insert takes a fully-formed domain model (A44=A).
 */
interface GrowingSpaceRepository {

    fun observeGrowingSpacesInGarden(gardenId: String): Flow<List<GrowingSpace>>

    fun observeGrowingSpace(id: String): Flow<GrowingSpace?>

    suspend fun getGrowingSpace(id: String): GrowingSpace?

    suspend fun insert(growingSpace: GrowingSpace)
}