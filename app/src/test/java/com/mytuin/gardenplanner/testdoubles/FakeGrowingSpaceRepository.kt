package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
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

    override fun observeGrowingSpacesInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<GrowingSpace>> =
        store.map { rows ->
            rows
                .filter { it.gardenId == gardenId }
                .let { if (includeArchived) it else it.filter { s -> s.status != RecordStatus.ARCHIVED } }
        }

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
            throw NotFoundError("GrowingSpace", id)
        }
        store.value =
            store.value.map { space ->
                if (space.id == id) {
                    space.copy(status = status, updatedAt = at)
                } else {
                    space
                }
            }
    }

    fun snapshot(): List<GrowingSpace> = store.value

    fun updateCalls(): List<UpdateGeometryCall> = updateCalls.toList()
}
