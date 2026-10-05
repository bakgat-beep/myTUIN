package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.ActivityDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.HarvestLossDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.HarvestLoss
import com.mytuin.gardenplanner.domain.repository.HarvestLossRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * HarvestLoss has four foreign keys, so a bare
 * SQLiteConstraintException cannot say which one failed. This
 * implementation pre-checks each optional parent, following the
 * established pattern.
 *
 * Unlike Harvest, this repository does not create an Activity. A
 * loss is standalone by default (HL2 (b), HL12); an optional
 * activityId references an existing Activity without modifying it.
 *
 * No archive method. HarvestLoss has no status column.
 */
class HarvestLossRepositoryImpl
    @Inject
    constructor(
        private val harvestLossDao: HarvestLossDao,
        private val gardenDao: GardenDao,
        private val activityDao: ActivityDao,
        private val plantInstanceDao: PlantInstanceDao,
        private val growingSpaceDao: GrowingSpaceDao,
    ) : HarvestLossRepository {
        override fun observeHarvestLossesInGarden(gardenId: String): Flow<List<HarvestLoss>> =
            harvestLossDao
                .observeForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeHarvestLossesForPlantInstance(plantInstanceId: String): Flow<List<HarvestLoss>> =
            harvestLossDao
                .observeForPlantInstance(plantInstanceId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeHarvestLossesInGrowingSpace(growingSpaceId: String): Flow<List<HarvestLoss>> =
            harvestLossDao
                .observeForGrowingSpace(growingSpaceId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeHarvestLoss(id: String): Flow<HarvestLoss?> = harvestLossDao.observeById(id).map { it?.toDomain() }

        override suspend fun getHarvestLoss(id: String): HarvestLoss? = harvestLossDao.getById(id)?.toDomain()

        override suspend fun insert(harvestLoss: HarvestLoss) {
            requireGardenExists(harvestLoss.gardenId)
            harvestLoss.activityId?.let { requireActivityExists(it) }
            harvestLoss.plantInstanceId?.let { requirePlantInstanceExists(it) }
            harvestLoss.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            try {
                harvestLossDao.insert(harvestLoss.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "harvest_loss",
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

        private suspend fun requireActivityExists(activityId: String) {
            activityDao.getById(activityId)
                ?: throw ValidationError(
                    field = "activity_id",
                    reason = "Activity does not exist: $activityId",
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
