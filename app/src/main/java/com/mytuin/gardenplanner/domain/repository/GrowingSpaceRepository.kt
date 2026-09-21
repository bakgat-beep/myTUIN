package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import kotlinx.coroutines.flow.Flow

interface GrowingSpaceRepository {
    fun observeGrowingSpacesInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<GrowingSpace>>

    fun observeGrowingSpace(id: String): Flow<GrowingSpace?>

    suspend fun getGrowingSpace(id: String): GrowingSpace?

    suspend fun insert(growingSpace: GrowingSpace)

    suspend fun updateGeometry(
        id: String,
        newGeometry: Geometry?,
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
