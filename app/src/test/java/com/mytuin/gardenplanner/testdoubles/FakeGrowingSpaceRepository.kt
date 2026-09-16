package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeGrowingSpaceRepository : GrowingSpaceRepository {

    private val store = MutableStateFlow<List<GrowingSpace>>(emptyList())

    override fun observeGrowingSpacesInGarden(gardenId: String): Flow<List<GrowingSpace>> =
        store.map { rows -> rows.filter { it.gardenId == gardenId } }

    override fun observeGrowingSpace(id: String): Flow<GrowingSpace?> =
        store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getGrowingSpace(id: String): GrowingSpace? =
        store.value.firstOrNull { it.id == id }

    override suspend fun insert(growingSpace: GrowingSpace) {
        store.value = store.value + growingSpace
    }

    fun snapshot(): List<GrowingSpace> = store.value
}