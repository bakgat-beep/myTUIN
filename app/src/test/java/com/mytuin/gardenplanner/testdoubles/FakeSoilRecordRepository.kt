package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.SoilRecord
import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSoilRecordRepository : SoilRecordRepository {
    data class StatusChangeCall(
        val id: String,
        val status: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<SoilRecord>>(emptyList())
    private val statusChangeCalls = mutableListOf<StatusChangeCall>()

    override fun observeSoilRecordsInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<SoilRecord>> =
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

    override fun observeSoilRecord(id: String): Flow<SoilRecord?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getSoilRecord(id: String): SoilRecord? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(soilRecord: SoilRecord) {
        store.value = store.value + soilRecord
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
            throw NotFoundError("SoilRecord", id)
        }
        statusChangeCalls.add(StatusChangeCall(id, status, at))
        store.value =
            store.value.map { record ->
                if (record.id == id) {
                    record.copy(status = status, updatedAt = at)
                } else {
                    record
                }
            }
    }

    fun snapshot(): List<SoilRecord> = store.value

    fun statusChangeCalls(): List<StatusChangeCall> = statusChangeCalls.toList()
}
