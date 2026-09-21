package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAreaRepository : AreaRepository {
    data class StatusChangeCall(
        val id: String,
        val status: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Area>>(emptyList())
    private val statusChangeCalls = mutableListOf<StatusChangeCall>()

    override fun observeAreasInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Area>> =
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

    override fun observeArea(id: String): Flow<Area?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getArea(id: String): Area? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(area: Area) {
        store.value = store.value + area
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
            throw NotFoundError("Area", id)
        }
        statusChangeCalls.add(StatusChangeCall(id, status, at))
        store.value =
            store.value.map { area ->
                if (area.id == id) {
                    area.copy(status = status, updatedAt = at)
                } else {
                    area
                }
            }
    }

    fun snapshot(): List<Area> = store.value

    fun statusChangeCalls(): List<StatusChangeCall> = statusChangeCalls.toList()
}
