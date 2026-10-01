package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Observation
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Observation.
 *
 * Three read scopes plus point lookup.
 *
 * insert is transactional: it writes both the Observation and its
 * linked timeline Activity in a single transaction (S1 (ii)).
 *
 * archive cascades through the linked Activity (O5): the Observation
 * has no status column, so archiving the Observation archives its
 * Activity, which removes it from default observation queries.
 */
interface ObservationRepository {
    fun observeObservationsInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Observation>>

    fun observeObservationsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Observation>>

    fun observeObservationsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Observation>>

    fun observeObservation(id: String): Flow<Observation?>

    suspend fun getObservation(id: String): Observation?

    suspend fun insert(observation: Observation)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )
}
