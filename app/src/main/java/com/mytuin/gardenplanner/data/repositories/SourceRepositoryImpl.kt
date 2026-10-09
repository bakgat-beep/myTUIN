package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.SourceDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Source
import com.mytuin.gardenplanner.domain.repository.SourceRepository
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Source has no foreign keys (SC1), so insert has no FK pre-checks.
 */
class SourceRepositoryImpl
    @Inject
    constructor(
        private val sourceDao: SourceDao,
    ) : SourceRepository {
        override fun observeAllSources(): Flow<List<Source>> = sourceDao.observeAll().map { rows -> rows.map { it.toDomain() } }

        override fun observeSource(id: String): Flow<Source?> = sourceDao.observeById(id).map { it?.toDomain() }

        override suspend fun getSource(id: String): Source? = sourceDao.getById(id)?.toDomain()

        override suspend fun insert(source: Source) {
            sourceDao.insert(source.toEntity())
        }

        override suspend fun updateStatus(
            id: String,
            status: SourceStatus,
        ) {
            val rows = sourceDao.updateStatus(id, status)
            if (rows == 0) {
                throw NotFoundError("Source", id)
            }
        }
    }
