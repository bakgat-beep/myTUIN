package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Activity
import com.mytuin.gardenplanner.domain.repository.ActivityRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeActivityRepository : ActivityRepository {
    data class StatusChangeCall(
        val id: String,
        val status: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Activity>>(emptyList())
    private val statusChangeCalls = mutableListOf<StatusChangeCall>()

    override fun observeActivitiesInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Activity>> =
        store.map { rows ->
            rows
                .filter { it.gardenId == gardenId }
                .let { filtered ->
                    if (includeArchived) {
                        filtered
                    } else {
                        filtered.filter { it.status != RecordStatus.ARCHIVED }
                    }
                }
        }

    override fun observeActivitiesForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean,
    ): Flow<List<Activity>> =
        store.map { rows ->
            rows
                .filter { it.plantInstanceId == plantInstanceId }
                .let { filtered ->
                    if (includeArchived) {
                        filtered
                    } else {
                        filtered.filter { it.status != RecordStatus.ARCHIVED }
                    }
                }
        }

    override fun observeActivitiesInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean,
    ): Flow<List<Activity>> =
        store.map { rows ->
            rows
                .filter { it.growingSpaceId == growingSpaceId }
                .let { filtered ->
                    if (includeArchived) {
                        filtered
                    } else {
                        filtered.filter { it.status != RecordStatus.ARCHIVED }
                    }
                }
        }

    override fun observeActivity(id: String): Flow<Activity?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getActivity(id: String): Activity? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(activity: Activity) {
        store.value = store.value + activity
    }

    override suspend fun archive(
        id: String,
        archivedAt: Long,
    ) {
        applyStatus(id, RecordStatus.ARCHIVED, archivedAt)
    }

    override suspend fun restore(
        id: String,
        restoredAt: Long,
    ) {
        applyStatus(id, RecordStatus.ACTIVE, restoredAt)
    }

    private fun applyStatus(
        id: String,
        status: RecordStatus,
        at: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Activity", id)
        }
        statusChangeCalls.add(StatusChangeCall(id, status, at))
        store.value =
            store.value.map { activity ->
                if (activity.id == id) {
                    activity.copy(status = status)
                } else {
                    activity
                }
            }
    }

    fun snapshot(): List<Activity> = store.value

    fun statusChangeCalls(): List<StatusChangeCall> = statusChangeCalls.toList()
}
