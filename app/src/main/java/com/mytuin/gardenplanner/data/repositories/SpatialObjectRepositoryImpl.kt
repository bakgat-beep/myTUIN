package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SpatialObject has two foreign keys, so a bare
 * SQLiteConstraintException cannot say which one failed. This
 * implementation pre-checks both parents, following the pattern
 * already established by GardenPreferenceRepositoryImpl, so the
 * ValidationError names the offending field. The database
 * constraints remain as backstops.
 */
class SpatialObjectRepositoryImpl
    @Inject
    constructor(
        private val spatialObjectDao: SpatialObjectDao,
        private val gardenDao: GardenDao,
        private val areaDao: AreaDao,
    ) : SpatialObjectRepository {
        override fun observeSpatialObjectsInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<SpatialObject>> {
            val source =
                if (includeArchived) {
                    spatialObjectDao.observeAllForGarden(gardenId)
                } else {
                    spatialObjectDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeSpatialObject(id: String): Flow<SpatialObject?> = spatialObjectDao.observeById(id).map { it?.toDomain() }

        override suspend fun getSpatialObject(id: String): SpatialObject? = spatialObjectDao.getById(id)?.toDomain()

        override suspend fun insert(spatialObject: SpatialObject) {
            requireGardenExists(spatialObject.gardenId)
            spatialObject.areaId?.let { requireAreaExists(it) }
            try {
                spatialObjectDao.insert(spatialObject.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "spatial_object",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = spatialObjectDao.updateStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("SpatialObject", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = spatialObjectDao.updateStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("SpatialObject", id)
            }
        }

        private suspend fun requireGardenExists(gardenId: String) {
            gardenDao.getById(gardenId)
                ?: throw ValidationError(
                    field = "garden_id",
                    reason = "Garden does not exist: $gardenId",
                )
        }

        private suspend fun requireAreaExists(areaId: String) {
            areaDao.getById(areaId)
                ?: throw ValidationError(
                    field = "area_id",
                    reason = "Area does not exist: $areaId",
                )
        }
    }
