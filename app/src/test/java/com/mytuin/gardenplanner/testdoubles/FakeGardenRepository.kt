package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory fake repository for use case tests.
 *
 * TESTING_STRATEGY §78 accepts fakes for isolating a use case from
 * the persistence layer.
 */
class FakeGardenRepository : GardenRepository {

    private val store = MutableStateFlow<List<Garden>>(emptyList())

    override fun observeGardens(): Flow<List<Garden>> = store

    override fun observeGarden(id: String): Flow<Garden?> =
        store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getGarden(id: String): Garden? =
        store.value.firstOrNull { it.id == id }

    override suspend fun insert(garden: Garden) {
        store.value = store.value + garden
    }

    /** Test-only accessor. */
    fun snapshot(): List<Garden> = store.value
}