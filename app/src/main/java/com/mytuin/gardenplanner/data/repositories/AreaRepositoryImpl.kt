package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AreaRepositoryImpl
    @Inject
    constructor(
        private val areaDao: AreaDao,
    ) : AreaRepository {
        override fun observeAreasInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<Area>> {
            val source =
                if (includeArchived) {
                    areaDao.observeAllForGarden(gardenId)
                } else {
                    areaDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeArea(id: String): Flow<Area?> = areaDao.observeById(id).map { it?.toDomain() }

        override suspend fun getArea(id: String): Area? = areaDao.getById(id)?.toDomain()

        override suspend fun insert(area: Area) {
            try {
                areaDao.insert(area.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "garden_id",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = areaDao.updateStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("Area", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = areaDao.updateStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("Area", id)
            }
        }
    }
