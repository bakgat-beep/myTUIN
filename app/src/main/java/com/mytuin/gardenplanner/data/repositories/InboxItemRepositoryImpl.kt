package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.InboxItemDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.InboxItem
import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InboxItemRepositoryImpl
    @Inject
    constructor(
        private val inboxItemDao: InboxItemDao,
        private val gardenDao: GardenDao,
    ) : InboxItemRepository {
        override fun observeInboxItemsInGarden(
            gardenId: String,
            includeClosed: Boolean,
        ): Flow<List<InboxItem>> {
            val source =
                if (includeClosed) {
                    inboxItemDao.observeAllForGarden(gardenId)
                } else {
                    inboxItemDao.observePendingForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeInboxItem(id: String): Flow<InboxItem?> = inboxItemDao.observeById(id).map { it?.toDomain() }

        override suspend fun getInboxItem(id: String): InboxItem? = inboxItemDao.getById(id)?.toDomain()

        override suspend fun insert(inboxItem: InboxItem) {
            gardenDao.getById(inboxItem.gardenId)
                ?: throw ValidationError(
                    field = "garden_id",
                    reason = "Garden does not exist: ${inboxItem.gardenId}",
                )
            try {
                inboxItemDao.insert(inboxItem.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "inbox_item",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun updateStatus(
            id: String,
            status: InboxItemStatus,
            updatedAt: Long,
        ) {
            val rows = inboxItemDao.updateStatus(id, status, updatedAt)
            if (rows == 0) {
                throw NotFoundError("InboxItem", id)
            }
        }

        override suspend fun mute(
            id: String,
            mutedUntil: Long?,
            updatedAt: Long,
        ) {
            val rows = inboxItemDao.updateMutedUntil(id, mutedUntil, updatedAt)
            if (rows == 0) {
                throw NotFoundError("InboxItem", id)
            }
        }
    }
