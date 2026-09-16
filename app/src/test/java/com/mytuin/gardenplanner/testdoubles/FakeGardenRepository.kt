package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeGardenRepository : GardenRepository {

    data class UpdateLocationCall(
        val id: String,
        val location: NewGardenLocation,
        val updatedAt: Long,
    )

    private val store = MutableStateFlow<List<Garden>>(emptyList())
    private val updateLocationCalls = mutableListOf<UpdateLocationCall>()

    override fun observeGardens(): Flow<List<Garden>> = store

    override fun observeGarden(id: String): Flow<Garden?> =
        store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getGarden(id: String): Garden? =
        store.value.firstOrNull { it.id == id }

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
        store.value = store.value.map { garden ->
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

    fun snapshot(): List<Garden> = store.value

    fun updateLocationCalls(): List<UpdateLocationCall> =
        updateLocationCalls.toList()
}