package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeGardenRepository : GardenRepository {
    data class UpdateLocationCall(
        val id: String,
        val location: NewGardenLocation,
        val updatedAt: Long,
    )

    data class StatusChangeCall(
        val id: String,
        val status: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Garden>>(emptyList())
    private val updateLocationCalls = mutableListOf<UpdateLocationCall>()
    private val statusChangeCalls = mutableListOf<StatusChangeCall>()

    override fun observeGardens(includeArchived: Boolean): Flow<List<Garden>> =
        store.map { rows ->
            if (includeArchived) {
                rows
            } else {
                rows.filter { it.status != RecordStatus.ARCHIVED }
            }
        }

    override fun observeGarden(id: String): Flow<Garden?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getGarden(id: String): Garden? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(garden: Garden) {
        store.value = store.value + garden
    }

    override suspend fun updateLocation(
        id: String,
        location: NewGardenLocation,
        updatedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Garden", id)
        }
        updateLocationCalls.add(UpdateLocationCall(id, location, updatedAt))
        store.value =
            store.value.map { garden ->
                if (garden.id == id) {
                    garden.copy(
                        countryCode = location.countryCode,
                        region = location.region,
                        locality = location.locality,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        timezone = location.timezone,
                        hemisphere = location.hemisphere,
                        updatedAt = updatedAt,
                    )
                } else {
                    garden
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
            throw NotFoundError("Garden", id)
        }
        statusChangeCalls.add(StatusChangeCall(id, status, at))
        store.value =
            store.value.map { garden ->
                if (garden.id == id) {
                    garden.copy(status = status, updatedAt = at)
                } else {
                    garden
                }
            }
    }

    fun snapshot(): List<Garden> = store.value

    fun updateLocationCalls(): List<UpdateLocationCall> = updateLocationCalls.toList()

    fun statusChangeCalls(): List<StatusChangeCall> = statusChangeCalls.toList()
}
