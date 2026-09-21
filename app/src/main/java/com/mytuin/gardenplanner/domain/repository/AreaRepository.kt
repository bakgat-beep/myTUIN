package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Area
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Area.
 *
 * Mirrors GrowingSpaceRepository. No updateGeometry yet: no
 * requirement, and adding it would imply a history-table decision
 * that DEC-041 does not currently make for Area (A4).
 */
interface AreaRepository {
    fun observeAreasInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Area>>

    fun observeArea(id: String): Flow<Area?>

    suspend fun getArea(id: String): Area?

    suspend fun insert(area: Area)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )
}
