package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.InboxItem
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for InboxItem.
 *
 * observeInboxItemsInGarden excludes dismissed and completed items
 * by default; `includeClosed = true` returns all rows.
 *
 * updateStatus transitions between pending and the terminal states
 * (dismissed, completed). mute sets mutedUntil; passing null clears
 * it. Both write updatedAt.
 *
 * No archive/restore: the lifecycle is captured by InboxItemStatus.
 */
interface InboxItemRepository {
    fun observeInboxItemsInGarden(
        gardenId: String,
        includeClosed: Boolean = false,
    ): Flow<List<InboxItem>>

    fun observeInboxItem(id: String): Flow<InboxItem?>

    suspend fun getInboxItem(id: String): InboxItem?

    suspend fun insert(inboxItem: InboxItem)

    suspend fun updateStatus(
        id: String,
        status: InboxItemStatus,
        updatedAt: Long,
    )

    suspend fun mute(
        id: String,
        mutedUntil: Long?,
        updatedAt: Long,
    )
}
