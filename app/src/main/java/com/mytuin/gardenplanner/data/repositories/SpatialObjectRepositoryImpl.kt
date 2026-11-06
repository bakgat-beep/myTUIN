package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import androidx.room.withTransaction
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectHistoryDao
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.entities.SpatialObjectHistoryEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields
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
 *
 * updateGeometry follows GrowingSpaceRepositoryImpl exactly: writes
 * the prior geometry and dimensions (none here — SpatialObject has
 * none) to the companion history table in the same transaction as
 * the current-state update, per DEC-041 / DEC-051.
 */
class SpatialObjectRepositoryImpl
    @Inject
    constructor(
        private val db: GardenDatabase,
        private val spatialObjectDao: SpatialObjectDao,
        private val spatialObjectHistoryDao: SpatialObjectHistoryDao,
        private val idGenerator: IdGenerator,
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

        override suspend fun updateGeometry(
            id: String,
            newGeometry: Geometry,
            effectiveAt: Long,
            reason: String?,
        ) {
            db.withTransaction {
                val current =
                    spatialObjectDao.getById(id)
                        ?: throw NotFoundError("SpatialObject", id)

                val previousHistory = spatialObjectHistoryDao.getLatestForObject(id)
                val validFrom = previousHistory?.valid_to ?: current.created_at

                spatialObjectHistoryDao.insert(
                    SpatialObjectHistoryEntity(
                        id = idGenerator.newSpatialObjectHistoryId(),
                        spatial_object_id = current.id,
                        geometry_type = current.geometry_type,
                        geometry_data = current.geometry_data,
                        valid_from = validFrom,
                        valid_to = effectiveAt,
                        recorded_at = effectiveAt,
                        reason = reason,
                    ),
                )

                spatialObjectDao.updateGeometry(
                    id = id,
                    geometryType = newGeometry.geometryType,
                    geometryData = GeometryGeoJson.encode(newGeometry),
                    updatedAt = effectiveAt,
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

        override suspend fun updateFields(
            id: String,
            fields: SpatialObjectFields,
            updatedAt: Long,
        ) {
            fields.areaId?.let { requireAreaExists(it) }
            val rows =
                spatialObjectDao.updateFields(
                    id = id,
                    objectType = fields.objectType,
                    name = fields.name,
                    description = fields.description,
                    areaId = fields.areaId,
                    notes = fields.notes,
                    updatedAt = updatedAt,
                )
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
