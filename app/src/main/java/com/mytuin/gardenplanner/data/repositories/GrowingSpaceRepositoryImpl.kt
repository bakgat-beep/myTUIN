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
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Room-backed GrowingSpace repository.
 *
 * A98=a: the repository is the boundary between the domain and
 * infrastructure. Low-level exceptions from Room are caught here and
 * re-thrown as DomainError subclasses; callers above this layer never
 * see SQLiteConstraintException.
 *
 * updateGeometry runs inside GardenDatabase.withTransaction (A67=a).
 * A missing space throws NotFoundError before any write occurs, so
 * the transaction is left untouched and history remains consistent.
 *
 * GardenDatabase is injected directly because Room's withTransaction
 * is a method on RoomDatabase, not on a DAO.
 */
class GrowingSpaceRepositoryImpl
    @Inject
    constructor(
        private val db: GardenDatabase,
        private val growingSpaceDao: GrowingSpaceDao,
        private val growingSpaceHistoryDao: GrowingSpaceHistoryDao,
        private val idGenerator: IdGenerator,
    ) : GrowingSpaceRepository {
        override fun observeGrowingSpacesInGarden(gardenId: String): Flow<List<GrowingSpace>> =
            growingSpaceDao
                .observeForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeGrowingSpace(id: String): Flow<GrowingSpace?> = growingSpaceDao.observeById(id).map { it?.toDomain() }

        override suspend fun getGrowingSpace(id: String): GrowingSpace? = growingSpaceDao.getById(id)?.toDomain()

        override suspend fun insert(growingSpace: GrowingSpace) {
            try {
                growingSpaceDao.insert(growingSpace.toEntity())
            } catch (e: SQLiteConstraintException) {
                // The GrowingSpace table has exactly one foreign key
                // (garden_id) and no unique constraint today. A constraint
                // failure on insert therefore means the referenced garden
                // does not exist. When additional constraints are added,
                // this attribution needs refining — inspect e.message.
                throw ValidationError(
                    field = "garden_id",
                    reason = e.message ?: "constraint violation",
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
    }
