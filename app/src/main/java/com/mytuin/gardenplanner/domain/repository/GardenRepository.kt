package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Garden.
 *
 * A125=a: updateLocation takes the id, the new location, and the
 * effective updatedAt. It replaces the seven location fields on the
 * row; a null field overwrites. It does not touch id, name,
 * description, status, or createdAt.
 *
 * A127=a: throws NotFoundError if no Garden with [id] exists.
 */
interface GardenRepository {
    fun observeGardens(): Flow<List<Garden>>

    fun observeGarden(id: String): Flow<Garden?>

    suspend fun getGarden(id: String): Garden?

    suspend fun insert(garden: Garden)

    suspend fun updateLocation(
        id: String,
        location: NewGardenLocation,
        updatedAt: Long,
    )
}
