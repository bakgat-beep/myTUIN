package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for GrowingSpace.
 *
 * Two update paths with distinct responsibilities (DEC-041):
 *   - updateGeometry writes the current geometry and a companion
 *     history row atomically.
 *   - updateFields writes only the mutable scalar fields; geometry,
 *     status, garden_id and created_at are preserved by the DAO
 *     query.
 */
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

    suspend fun updateFields(
        id: String,
        fields: GrowingSpaceFields,
        updatedAt: Long,
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
