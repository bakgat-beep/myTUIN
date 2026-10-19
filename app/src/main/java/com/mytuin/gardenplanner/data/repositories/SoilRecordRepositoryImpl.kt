package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.SoilRecordDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.SoilRecord
import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SoilRecordRepositoryImpl.
 *
 * Pre-checks the three FK parents (garden, area, growing space) so a
 * missing parent surfaces as ValidationError with the offending
 * field named rather than an opaque constraint failure.
 */
class SoilRecordRepositoryImpl
    @Inject
    constructor(
        private val soilRecordDao: SoilRecordDao,
        private val gardenDao: GardenDao,
        private val areaDao: AreaDao,
        private val growingSpaceDao: GrowingSpaceDao,
    ) : SoilRecordRepository {
        override fun observeSoilRecordsInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<SoilRecord>> {
            val source =
                if (includeArchived) {
                    soilRecordDao.observeAllForGarden(gardenId)
                } else {
                    soilRecordDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeSoilRecord(id: String): Flow<SoilRecord?> = soilRecordDao.observeById(id).map { it?.toDomain() }

        override suspend fun getSoilRecord(id: String): SoilRecord? = soilRecordDao.getById(id)?.toDomain()

        override suspend fun insert(soilRecord: SoilRecord) {
            requireGardenExists(soilRecord.gardenId)
            soilRecord.areaId?.let { requireAreaExists(it) }
            soilRecord.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            try {
                soilRecordDao.insert(soilRecord.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "soil_record",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = soilRecordDao.updateStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("SoilRecord", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = soilRecordDao.updateStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("SoilRecord", id)
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

        private suspend fun requireGrowingSpaceExists(growingSpaceId: String) {
            growingSpaceDao.getById(growingSpaceId)
                ?: throw ValidationError(
                    field = "growing_space_id",
                    reason = "GrowingSpace does not exist: $growingSpaceId",
                )
        }
    }
