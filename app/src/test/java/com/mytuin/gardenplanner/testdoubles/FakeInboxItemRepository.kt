package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.InboxItem
import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeInboxItemRepository : InboxItemRepository {
    private val store = MutableStateFlow<List<InboxItem>>(emptyList())

    override fun observeInboxItemsInGarden(
        gardenId: String,
        includeClosed: Boolean,
    ): Flow<List<InboxItem>> =
        store.map { rows ->
            rows
                .filter { it.gardenId == gardenId }
                .let { filtered ->
                    if (includeClosed) {
                        filtered
                    } else {
                        filtered.filter { it.status == InboxItemStatus.PENDING }
                    }
                }
        }

    override fun observeInboxItem(id: String): Flow<InboxItem?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getInboxItem(id: String): InboxItem? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(inboxItem: InboxItem) {
        store.value = store.value + inboxItem
    }

    override suspend fun updateStatus(
        id: String,
        status: InboxItemStatus,
        updatedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("InboxItem", id)
        }
        store.value =
            store.value.map { row ->
                if (row.id == id) {
                    row.copy(status = status, updatedAt = updatedAt)
                } else {
                    row
                }
            }
    }

    override suspend fun mute(
        id: String,
        mutedUntil: Long?,
        updatedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("InboxItem", id)
        }
        store.value =
            store.value.map { row ->
                if (row.id == id) {
                    row.copy(mutedUntil = mutedUntil, updatedAt = updatedAt)
                } else {
                    row
                }
            }
    }

    fun snapshot(): List<InboxItem> = store.value
}
