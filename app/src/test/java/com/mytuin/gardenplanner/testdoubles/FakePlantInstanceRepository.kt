package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstanceLocation
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlantInstanceRepository : PlantInstanceRepository {
    data class StatusChangeCall(
        val id: String,
        val status: RecordStatus,
        val at: Long,
    )

    data class LocationChangeCall(
        val id: String,
        val location: NewPlantInstanceLocation,
        val effectiveAt: Long,
        val reason: String?,
    )

    private val store = MutableStateFlow<List<PlantInstance>>(emptyList())
    private val statusChangeCalls = mutableListOf<StatusChangeCall>()
    private val locationChangeCalls = mutableListOf<LocationChangeCall>()

    override fun observePlantInstancesInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<PlantInstance>> =
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

    override fun observePlantInstancesInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean,
    ): Flow<List<PlantInstance>> =
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

    override fun observePlantInstance(id: String): Flow<PlantInstance?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getPlantInstance(id: String): PlantInstance? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(plantInstance: PlantInstance) {
        store.value = store.value + plantInstance
    }

    override suspend fun updateLocation(
        id: String,
        location: NewPlantInstanceLocation,
        effectiveAt: Long,
        reason: String?,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("PlantInstance", id)
        }
        locationChangeCalls.add(LocationChangeCall(id, location, effectiveAt, reason))
        store.value =
            store.value.map { instance ->
                if (instance.id == id) {
                    instance.copy(
                        growingSpaceId = location.growingSpaceId,
                        spatialObjectId = location.spatialObjectId,
                        geometry = location.geometry,
                        updatedAt = effectiveAt,
                    )
                } else {
                    instance
                }
            }
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
            throw NotFoundError("PlantInstance", id)
        }
        statusChangeCalls.add(StatusChangeCall(id, status, at))
        store.value =
            store.value.map { plantInstance ->
                if (plantInstance.id == id) {
                    plantInstance.copy(status = status, updatedAt = at)
                } else {
                    plantInstance
                }
            }
    }

    fun snapshot(): List<PlantInstance> = store.value

    fun statusChangeCalls(): List<StatusChangeCall> = statusChangeCalls.toList()

    fun locationChangeCalls(): List<LocationChangeCall> = locationChangeCalls.toList()
}
