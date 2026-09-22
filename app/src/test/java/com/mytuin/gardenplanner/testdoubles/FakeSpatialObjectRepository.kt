package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSpatialObjectRepository : SpatialObjectRepository {
    data class StatusChangeCall(
        val id: String,
        val status: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<SpatialObject>>(emptyList())
    private val statusChangeCalls = mutableListOf<StatusChangeCall>()

    override fun observeSpatialObjectsInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<SpatialObject>> =
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

    override fun observeSpatialObject(id: String): Flow<SpatialObject?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getSpatialObject(id: String): SpatialObject? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(spatialObject: SpatialObject) {
        store.value = store.value + spatialObject
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
            throw NotFoundError("SpatialObject", id)
        }
        statusChangeCalls.add(StatusChangeCall(id, status, at))
        store.value =
            store.value.map { spatialObject ->
                if (spatialObject.id == id) {
                    spatialObject.copy(status = status, updatedAt = at)
                } else {
                    spatialObject
                }
            }
    }

    fun snapshot(): List<SpatialObject> = store.value

    fun statusChangeCalls(): List<StatusChangeCall> = statusChangeCalls.toList()
}
