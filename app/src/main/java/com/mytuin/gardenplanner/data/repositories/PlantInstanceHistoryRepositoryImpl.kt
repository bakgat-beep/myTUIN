package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.PlantInstanceHistoryDao
import com.mytuin.gardenplanner.domain.model.garden.PlantInstanceHistory
import com.mytuin.gardenplanner.domain.repository.PlantInstanceHistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlantInstanceHistoryRepositoryImpl
    @Inject
    constructor(
        private val plantInstanceHistoryDao: PlantInstanceHistoryDao,
    ) : PlantInstanceHistoryRepository {
        override fun observeHistoryFor(plantInstanceId: String): Flow<List<PlantInstanceHistory>> =
            plantInstanceHistoryDao
                .observeForInstance(plantInstanceId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun getHistoryFor(plantInstanceId: String): List<PlantInstanceHistory> =
            plantInstanceHistoryDao.getForInstance(plantInstanceId).map { it.toDomain() }

        override suspend fun getHistoryAsOf(
            plantInstanceId: String,
            atMillis: Long,
        ): PlantInstanceHistory? = plantInstanceHistoryDao.getAsOf(plantInstanceId, atMillis)?.toDomain()
    }
