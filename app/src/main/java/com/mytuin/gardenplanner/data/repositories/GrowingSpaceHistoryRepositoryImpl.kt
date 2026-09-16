package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GrowingSpaceHistoryRepositoryImpl @Inject constructor(
    private val growingSpaceHistoryDao: GrowingSpaceHistoryDao,
) : GrowingSpaceHistoryRepository {

    override fun observeHistoryForSpace(growingSpaceId: String): Flow<List<GrowingSpaceHistory>> =
        growingSpaceHistoryDao.observeForSpace(growingSpaceId)
            .map { rows -> rows.map { it.toDomain() } }

    override suspend fun getHistoryForSpace(growingSpaceId: String): List<GrowingSpaceHistory> =
        growingSpaceHistoryDao.getForSpace(growingSpaceId).map { it.toDomain() }

    override suspend fun getHistoryAsOf(
        growingSpaceId: String,
        atMillis: Long,
    ): GrowingSpaceHistory? =
        growingSpaceHistoryDao.getAsOf(growingSpaceId, atMillis)?.toDomain()
}