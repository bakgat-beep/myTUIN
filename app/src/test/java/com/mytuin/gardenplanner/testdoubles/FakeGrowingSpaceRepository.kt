package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeGrowingSpaceRepository : GrowingSpaceRepository {
    data class UpdateGeometryCall(
        val id: String,
        val newGeometry: Geometry?,
        val effectiveAt: Long,
        val reason: String?,
    )

    private val store = MutableStateFlow<List<GrowingSpace>>(emptyList())
    private val updateCalls = mutableListOf<UpdateGeometryCall>()

    override fun observeGrowingSpacesInGarden(gardenId: String): Flow<List<GrowingSpace>> =
        store.map { rows -> rows.filter { it.gardenId == gardenId } }

    override fun observeGrowingSpace(id: String): Flow<GrowingSpace?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getGrowingSpace(id: String): GrowingSpace? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(growingSpace: GrowingSpace) {
        store.value = store.value + growingSpace
    }

    override suspend fun updateGeometry(
        id: String,
        newGeometry: Geometry?,
        effectiveAt: Long,
        reason: String?,
    ) {
        if (store.value.none { it.id == id }) {
            // A101: match the real repository's error type.
            throw NotFoundError("GrowingSpace", id)
        }
        updateCalls.add(UpdateGeometryCall(id, newGeometry, effectiveAt, reason))
        store.value =
            store.value.map { space ->
                if (space.id == id) {
                    space.copy(geometry = newGeometry, updatedAt = effectiveAt)
                } else {
                    space
                }
            }
    }

    fun snapshot(): List<GrowingSpace> = store.value

    fun updateCalls(): List<UpdateGeometryCall> = updateCalls.toList()
}
