package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.MeasurementDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Measurement
import com.mytuin.gardenplanner.domain.repository.MeasurementRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Measurement has five foreign keys, so a bare
 * SQLiteConstraintException cannot say which one failed. This
 * implementation pre-checks each optional parent, following the
 * established pattern.
 *
 * No archive or restore. Measurement has no status column (M7, M8).
 */
class MeasurementRepositoryImpl
    @Inject
    constructor(
        private val measurementDao: MeasurementDao,
        private val gardenDao: GardenDao,
        private val areaDao: AreaDao,
        private val growingSpaceDao: GrowingSpaceDao,
        private val spatialObjectDao: SpatialObjectDao,
        private val plantInstanceDao: PlantInstanceDao,
    ) : MeasurementRepository {
        override fun observeMeasurementsInGarden(gardenId: String): Flow<List<Measurement>> =
            measurementDao
                .observeForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeMeasurementsForPlantInstance(plantInstanceId: String): Flow<List<Measurement>> =
            measurementDao
                .observeForPlantInstance(plantInstanceId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeMeasurementsInGrowingSpace(growingSpaceId: String): Flow<List<Measurement>> =
            measurementDao
                .observeForGrowingSpace(growingSpaceId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeMeasurement(id: String): Flow<Measurement?> = measurementDao.observeById(id).map { it?.toDomain() }

        override suspend fun getMeasurement(id: String): Measurement? = measurementDao.getById(id)?.toDomain()

        override suspend fun insert(measurement: Measurement) {
            requireGardenExists(measurement.gardenId)
            measurement.areaId?.let { requireAreaExists(it) }
            measurement.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            measurement.spatialObjectId?.let { requireSpatialObjectExists(it) }
            measurement.plantInstanceId?.let { requirePlantInstanceExists(it) }
            try {
                measurementDao.insert(measurement.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "measurement",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
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

        private suspend fun requireSpatialObjectExists(spatialObjectId: String) {
            spatialObjectDao.getById(spatialObjectId)
                ?: throw ValidationError(
                    field = "spatial_object_id",
                    reason = "SpatialObject does not exist: $spatialObjectId",
                )
        }

        private suspend fun requirePlantInstanceExists(plantInstanceId: String) {
            plantInstanceDao.getById(plantInstanceId)
                ?: throw ValidationError(
                    field = "plant_instance_id",
                    reason = "PlantInstance does not exist: $plantInstanceId",
                )
        }
    }
