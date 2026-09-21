package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Garden.
 *
 * A231=a: observeGardens excludes archived by default. Callers that
 * need archived gardens pass includeArchived = true.
 *
 * A234: archive and restore return Unit. A239: a missing garden
 * throws NotFoundError.
 */
interface GardenRepository {
    fun observeGardens(includeArchived: Boolean = false): Flow<List<Garden>>

    fun observeGarden(id: String): Flow<Garden?>

    suspend fun getGarden(id: String): Garden?

    suspend fun insert(garden: Garden)

    suspend fun updateLocation(
        id: String,
        location: NewGardenLocation,
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
