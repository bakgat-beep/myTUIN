package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.SpatialObjectHistoryDao
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.repository.SpatialObjectHistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpatialObjectHistoryRepositoryImpl
    @Inject
    constructor(
        private val spatialObjectHistoryDao: SpatialObjectHistoryDao,
    ) : SpatialObjectHistoryRepository {
        override fun observeHistoryForObject(spatialObjectId: String): Flow<List<SpatialObjectHistory>> =
            spatialObjectHistoryDao
                .observeForObject(spatialObjectId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun getHistoryForObject(spatialObjectId: String): List<SpatialObjectHistory> =
            spatialObjectHistoryDao.getForObject(spatialObjectId).map { it.toDomain() }

        override suspend fun getHistoryAsOf(
            spatialObjectId: String,
            atMillis: Long,
        ): SpatialObjectHistory? = spatialObjectHistoryDao.getAsOf(spatialObjectId, atMillis)?.toDomain()
    }
