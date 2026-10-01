package com.mytuin.gardenplanner.data.repositories

import androidx.room.withTransaction
import com.mytuin.gardenplanner.data.dao.ActivityDao
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.ObservationDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Observation
import com.mytuin.gardenplanner.domain.repository.ObservationRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ObservationRepository owns the transaction that creates both the
 * Observation and its linked timeline Activity (S1 (ii)). Following
 * GrowingSpaceRepositoryImpl.updateGeometry, the transaction spans
 * two DAOs inside one repository method.
 *
 * The Activity is derived from the Observation:
 *   - id = observation.activityId
 *   - activity_type = observation
 *   - occurred_at = observedAt
 *   - location refs = the Observation's
 *   - status = active
 *   - notes = null; the note lives on the Observation
 *
 * Archive cascades through the linked Activity (O5): the
 * Observation has no status column.
 */
class ObservationRepositoryImpl
@Inject
constructor(
    private val db: GardenDatabase,
    private val observationDao: ObservationDao,
    private val activityDao: ActivityDao,
    private val gardenDao: GardenDao,
    private val areaDao: AreaDao,
    private val growingSpaceDao: GrowingSpaceDao,
    private val spatialObjectDao: SpatialObjectDao,
    private val plantInstanceDao: PlantInstanceDao,
) : ObservationRepository {
    override fun observeObservationsInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Observation>> {
        val source =
            if (includeArchived) {
                observationDao.observeAllForGarden(gardenId)
            } else {
                observationDao.observeActiveForGarden(gardenId)
            }
        return source.map { rows -> rows.map { it.toDomain() } }
    }

    override fun observeObservationsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean,
    ): Flow<List<Observation>> {
        val source =
            if (includeArchived) {
                observationDao.observeAllForPlantInstance(plantInstanceId)
            } else {
                observationDao.observeActiveForPlantInstance(plantInstanceId)
            }
        return source.map { rows -> rows.map { it.toDomain() } }
    }

    override fun observeObservationsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean,
    ): Flow<List<Observation>> {
        val source =
            if (includeArchived) {
                observationDao.observeAllForGrowingSpace(growingSpaceId)
            } else {
                observationDao.observeActiveForGrowingSpace(growingSpaceId)
            }
        return source.map { rows -> rows.map { it.toDomain() } }
    }

    override fun observeObservation(id: String): Flow<Observation?> =
        observationDao.observeById(id).map { it?.toDomain() }

    override suspend fun getObservation(id: String): Observation? =
        observationDao.getById(id)?.toDomain()

    override suspend fun insert(observation: Observation) {
        db.withTransaction {
            requireGardenExists(observation.gardenId)
            observation.areaId?.let { requireAreaExists(it) }
            observation.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            observation.spatialObjectId?.let { requireSpatialObjectExists(it) }
            observation.plantInstanceId?.let { requirePlantInstanceExists(it) }

            activityDao.insert(activityFor(observation))
            observationDao.insert(observation.toEntity())
        }
    }

    override suspend fun archive(
        id: String,
        archivedAt: Long,
    ) {
        // archivedAt is accepted for interface symmetry but is not
        // persisted: activity has no updated_at column (S7).
        db.withTransaction {
            val observation =
                observationDao.getById(id)
                    ?: throw NotFoundError("Observation", id)

            val rows = activityDao.updateStatus(observation.activity_id, RecordStatus.ARCHIVED)
            if (rows == 0) {
                throw NotFoundError("Activity", observation.activity_id)
            }
        }
    }

    private fun activityFor(observation: Observation): ActivityEntity =
        ActivityEntity(
            id = observation.activityId,
            garden_id = observation.gardenId,
            activity_type = ActivityType.OBSERVATION,
            occurred_at = observation.observedAt,
            created_at = observation.createdAt,
            area_id = observation.areaId,
            growing_space_id = observation.growingSpaceId,
            spatial_object_id = observation.spatialObjectId,
            plant_instance_id = observation.plantInstanceId,
            quantity = null,
            unit = null,
            planting_method = null,
            watering_method = null,
            feeding_method = null,
            pruning_method = null,
            soil_work_method = null,
            data_origin = null,
            status = RecordStatus.ACTIVE,
            notes = null,
        )

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
