package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import androidx.room.withTransaction
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceHistoryDao
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.entities.GrowingSpaceHistoryEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GrowingSpaceRepositoryImpl
    @Inject
    constructor(
        private val db: GardenDatabase,
        private val growingSpaceDao: GrowingSpaceDao,
        private val growingSpaceHistoryDao: GrowingSpaceHistoryDao,
        private val idGenerator: IdGenerator,
    ) : GrowingSpaceRepository {
        override fun observeGrowingSpacesInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<GrowingSpace>> {
            val source =
                if (includeArchived) {
                    growingSpaceDao.observeAllForGarden(gardenId)
                } else {
                    growingSpaceDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeGrowingSpace(id: String): Flow<GrowingSpace?> = growingSpaceDao.observeById(id).map { it?.toDomain() }

        override suspend fun getGrowingSpace(id: String): GrowingSpace? = growingSpaceDao.getById(id)?.toDomain()

        override suspend fun insert(growingSpace: GrowingSpace) {
            try {
                growingSpaceDao.insert(growingSpace.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "garden_id",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun updateGeometry(
            id: String,
            newGeometry: Geometry?,
            effectiveAt: Long,
            reason: String?,
        ) {
            db.withTransaction {
                val current =
                    growingSpaceDao.getById(id)
                        ?: throw NotFoundError("GrowingSpace", id)

                val previousHistory = growingSpaceHistoryDao.getLatestForSpace(id)
                val validFrom = previousHistory?.valid_to ?: current.created_at

                growingSpaceHistoryDao.insert(
                    GrowingSpaceHistoryEntity(
                        id = idGenerator.newGrowingSpaceHistoryId(),
                        growing_space_id = current.id,
                        geometry_type = current.geometry_type,
                        geometry_data = current.geometry_data,
                        length = current.length,
                        width = current.width,
                        height = current.height,
                        diameter = current.diameter,
                        area = current.area,
                        volume = current.volume,
                        valid_from = validFrom,
                        valid_to = effectiveAt,
                        recorded_at = effectiveAt,
                        reason = reason,
                    ),
                )

                growingSpaceDao.updateGeometry(
                    id = id,
                    geometryType = newGeometry?.geometryType,
                    geometryData = newGeometry?.let { GeometryGeoJson.encode(it) },
                    updatedAt = effectiveAt,
                )
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = growingSpaceDao.updateStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("GrowingSpace", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = growingSpaceDao.updateStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("GrowingSpace", id)
            }
        }
    }
