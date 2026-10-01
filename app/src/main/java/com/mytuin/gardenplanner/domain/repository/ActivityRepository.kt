package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Activity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Activity.
 *
 * Three read scopes plus point lookup. Garden is the primary scope;
 * the plant-instance and growing-space queries are what Phase 4 will
 * need for the "history of this plant" and "history of this bed"
 * views.
 *
 * No update method. Event records are immutable once created (S7).
 * Corrections use the append-only mechanism described in
 * V1_DATABASE_SCHEMA §62, which is a later step.
 */
interface ActivityRepository {
    fun observeActivitiesInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Activity>>

    fun observeActivitiesForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Activity>>

    fun observeActivitiesInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Activity>>

    fun observeActivity(id: String): Flow<Activity?>

    suspend fun getActivity(id: String): Activity?

    suspend fun insert(activity: Activity)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )
}
