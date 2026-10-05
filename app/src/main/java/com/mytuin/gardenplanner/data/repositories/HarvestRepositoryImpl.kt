package com.mytuin.gardenplanner.data.repositories

import androidx.room.withTransaction
import com.mytuin.gardenplanner.data.dao.ActivityDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.HarvestDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Harvest
import com.mytuin.gardenplanner.domain.repository.HarvestRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * HarvestRepository owns the transaction that creates both the
 * Harvest and its linked harvesting Activity (HL1). Follows the
 * ObservationRepositoryImpl pattern.
 *
 * The Activity is derived from the Harvest:
 *   - id = harvest.activityId
 *   - activity_type = harvesting
 *   - occurred_at = normalized from the Harvest's date (see
 *     HarvestMapper.toActivityOccurredAtMillis; date-only becomes
 *     UTC midnight of that day)
 *   - location refs = plant_instance_id and growing_space_id only
 *     (§38 does not give Harvest an area or spatial-object link)
 *   - quantity/unit = the Harvest's
 *   - status = active
 *   - notes = null; the note lives on the Harvest
 *
 * Archive cascades through the linked Activity (HL11): Harvest has
 * no status column.
 */
class HarvestRepositoryImpl
    @Inject
    constructor(
        private val db: GardenDatabase,
        private val harvestDao: HarvestDao,
        private val activityDao: ActivityDao,
        private val gardenDao: GardenDao,
        private val plantInstanceDao: PlantInstanceDao,
        private val growingSpaceDao: GrowingSpaceDao,
    ) : HarvestRepository {
        override fun observeHarvestsInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<Harvest>> {
            val source =
                if (includeArchived) {
                    harvestDao.observeAllForGarden(gardenId)
                } else {
                    harvestDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeHarvestsForPlantInstance(
            plantInstanceId: String,
            includeArchived: Boolean,
        ): Flow<List<Harvest>> {
            val source =
                if (includeArchived) {
                    harvestDao.observeAllForPlantInstance(plantInstanceId)
                } else {
                    harvestDao.observeActiveForPlantInstance(plantInstanceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeHarvestsInGrowingSpace(
            growingSpaceId: String,
            includeArchived: Boolean,
        ): Flow<List<Harvest>> {
            val source =
                if (includeArchived) {
                    harvestDao.observeAllForGrowingSpace(growingSpaceId)
                } else {
                    harvestDao.observeActiveForGrowingSpace(growingSpaceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeHarvest(id: String): Flow<Harvest?> = harvestDao.observeById(id).map { it?.toDomain() }

        override suspend fun getHarvest(id: String): Harvest? = harvestDao.getById(id)?.toDomain()

        override suspend fun insert(harvest: Harvest) {
            db.withTransaction {
                requireGardenExists(harvest.gardenId)
                harvest.plantInstanceId?.let { requirePlantInstanceExists(it) }
                harvest.growingSpaceId?.let { requireGrowingSpaceExists(it) }

                activityDao.insert(activityFor(harvest))
                harvestDao.insert(harvest.toEntity())
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            // archivedAt is accepted for interface symmetry but is not
            // persisted: activity has no updated_at column (S7).
            db.withTransaction {
                val harvest =
                    harvestDao.getById(id)
                        ?: throw NotFoundError("Harvest", id)

                val rows = activityDao.updateStatus(harvest.activity_id, RecordStatus.ARCHIVED)
                if (rows == 0) {
                    throw NotFoundError("Activity", harvest.activity_id)
                }
            }
        }

        private fun activityFor(harvest: Harvest): ActivityEntity =
            ActivityEntity(
                id = harvest.activityId,
                garden_id = harvest.gardenId,
                activity_type = ActivityType.HARVESTING,
                occurred_at = harvest.date.toActivityOccurredAtMillis(),
                created_at = harvest.createdAt,
                area_id = null,
                growing_space_id = harvest.growingSpaceId,
                spatial_object_id = null,
                plant_instance_id = harvest.plantInstanceId,
                quantity = harvest.quantity,
                unit = harvest.unit,
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

        private suspend fun requirePlantInstanceExists(plantInstanceId: String) {
            plantInstanceDao.getById(plantInstanceId)
                ?: throw ValidationError(
                    field = "plant_instance_id",
                    reason = "PlantInstance does not exist: $plantInstanceId",
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
