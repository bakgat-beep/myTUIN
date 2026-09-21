package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GardenRepositoryImpl
    @Inject
    constructor(
        private val gardenDao: GardenDao,
    ) : GardenRepository {
        override fun observeGardens(includeArchived: Boolean): Flow<List<Garden>> {
            val source =
                if (includeArchived) {
                    gardenDao.observeAll()
                } else {
                    gardenDao.observeActive()
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeGarden(id: String): Flow<Garden?> = gardenDao.observeById(id).map { it?.toDomain() }

        override suspend fun getGarden(id: String): Garden? = gardenDao.getById(id)?.toDomain()

        override suspend fun insert(garden: Garden) = gardenDao.insert(garden.toEntity())

        override suspend fun updateLocation(
            id: String,
            location: NewGardenLocation,
            updatedAt: Long,
        ) {
            val rows =
                gardenDao.updateLocation(
                    id = id,
                    countryCode = location.countryCode,
                    region = location.region,
                    locality = location.locality,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    timezone = location.timezone,
                    hemisphere = location.hemisphere,
                    updatedAt = updatedAt,
                )
            if (rows == 0) {
                throw NotFoundError("Garden", id)
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = gardenDao.updateStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("Garden", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = gardenDao.updateStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("Garden", id)
            }
        }
    }
