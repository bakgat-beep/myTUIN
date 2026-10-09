package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Source
import com.mytuin.gardenplanner.domain.repository.SourceRepository
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSourceRepository : SourceRepository {
    data class StatusCall(
        val id: String,
        val status: SourceStatus,
    )

    private val store = MutableStateFlow<List<Source>>(emptyList())
    private val statusCalls = mutableListOf<StatusCall>()

    override fun observeAllSources(): Flow<List<Source>> = store.map { it }

    override fun observeSource(id: String): Flow<Source?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getSource(id: String): Source? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(source: Source) {
        store.value = store.value + source
    }

    override suspend fun updateStatus(
        id: String,
        status: SourceStatus,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Source", id)
        }
        statusCalls.add(StatusCall(id, status))
        store.value =
            store.value.map { source ->
                if (source.id == id) {
                    source.copy(status = status)
                } else {
                    source
                }
            }
    }

    fun snapshot(): List<Source> = store.value

    fun statusCalls(): List<StatusCall> = statusCalls.toList()
}
