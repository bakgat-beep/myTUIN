package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * PlantInstance has five foreign keys, so a bare
 * SQLiteConstraintException cannot say which one failed. This
 * implementation pre-checks each optional parent, following the
 * pattern established by SpatialObjectRepositoryImpl and
 * GardenPreferenceRepositoryImpl. The database constraints remain as
 * backstops.
 */
class PlantInstanceRepositoryImpl
    @Inject
    constructor(
        private val plantInstanceDao: PlantInstanceDao,
        private val gardenDao: GardenDao,
        private val plantDao: PlantDao,
        private val cultivarDao: CultivarDao,
        private val growingSpaceDao: GrowingSpaceDao,
        private val spatialObjectDao: SpatialObjectDao,
    ) : PlantInstanceRepository {
        override fun observePlantInstancesInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<PlantInstance>> {
            val source =
                if (includeArchived) {
                    plantInstanceDao.observeAllForGarden(gardenId)
                } else {
                    plantInstanceDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observePlantInstancesInGrowingSpace(
            growingSpaceId: String,
            includeArchived: Boolean,
        ): Flow<List<PlantInstance>> {
            val source =
                if (includeArchived) {
                    plantInstanceDao.observeAllForGrowingSpace(growingSpaceId)
                } else {
                    plantInstanceDao.observeActiveForGrowingSpace(growingSpaceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observePlantInstance(id: String): Flow<PlantInstance?> = plantInstanceDao.observeById(id).map { it?.toDomain() }

        override suspend fun getPlantInstance(id: String): PlantInstance? = plantInstanceDao.getById(id)?.toDomain()

        override suspend fun insert(plantInstance: PlantInstance) {
            requireGardenExists(plantInstance.gardenId)
            requirePlantExists(plantInstance.plantId)
            plantInstance.cultivarId?.let { requireCultivarExists(it) }
            plantInstance.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            plantInstance.spatialObjectId?.let { requireSpatialObjectExists(it) }
            try {
                plantInstanceDao.insert(plantInstance.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "plant_instance",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = plantInstanceDao.updateStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("PlantInstance", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = plantInstanceDao.updateStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("PlantInstance", id)
            }
        }

        private suspend fun requireGardenExists(gardenId: String) {
            gardenDao.getById(gardenId)
                ?: throw ValidationError(
                    field = "garden_id",
                    reason = "Garden does not exist: $gardenId",
                )
        }

        private suspend fun requirePlantExists(plantId: String) {
            plantDao.getById(plantId)
                ?: throw ValidationError(
                    field = "plant_id",
                    reason = "Plant does not exist: $plantId",
                )
        }

        private suspend fun requireCultivarExists(cultivarId: String) {
            cultivarDao.getById(cultivarId)
                ?: throw ValidationError(
                    field = "cultivar_id",
                    reason = "Cultivar does not exist: $cultivarId",
                )
        }

        private suspend fun requireGrowingSpaceExists(growingSpaceId: String) {
            growingSpaceDao.getById(growingSpaceId)
                ?: throw ValidationError(
                    field = "growing_space_id",
                    reason = "GrowingSpace does not exist: $growingSpaceId",
                )
        }

        private suspend fun requireSpatialObjectExists(spatialObjectId: String) {
            spatialObjectDao.getById(spatialObjectId)
                ?: throw ValidationError(
                    field = "spatial_object_id",
                    reason = "SpatialObject does not exist: $spatialObjectId",
                )
        }
    }
