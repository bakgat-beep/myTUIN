package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Activity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Activity.
 *
 * Four read scopes plus point lookup. `observeActivitiesForPlan`
 * (PL9) returns the Activities that completed a given Plan.
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

    fun observeActivitiesForPlan(
        planId: String,
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
