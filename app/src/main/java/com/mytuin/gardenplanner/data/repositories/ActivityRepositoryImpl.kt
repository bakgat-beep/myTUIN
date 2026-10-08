package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.ActivityDao
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.PlanDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Activity
import com.mytuin.gardenplanner.domain.repository.ActivityRepository
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Activity has six foreign keys, so a bare
 * SQLiteConstraintException cannot say which one failed. This
 * implementation pre-checks each optional parent, following the
 * established pattern.
 */
class ActivityRepositoryImpl
    @Inject
    constructor(
        private val activityDao: ActivityDao,
        private val gardenDao: GardenDao,
        private val areaDao: AreaDao,
        private val growingSpaceDao: GrowingSpaceDao,
        private val spatialObjectDao: SpatialObjectDao,
        private val plantInstanceDao: PlantInstanceDao,
        private val planDao: PlanDao,
    ) : ActivityRepository {
        override fun observeActivitiesInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<Activity>> {
            val source =
                if (includeArchived) {
                    activityDao.observeAllForGarden(gardenId)
                } else {
                    activityDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeActivitiesForPlantInstance(
            plantInstanceId: String,
            includeArchived: Boolean,
        ): Flow<List<Activity>> {
            val source =
                if (includeArchived) {
                    activityDao.observeAllForPlantInstance(plantInstanceId)
                } else {
                    activityDao.observeActiveForPlantInstance(plantInstanceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeActivitiesInGrowingSpace(
            growingSpaceId: String,
            includeArchived: Boolean,
        ): Flow<List<Activity>> {
            val source =
                if (includeArchived) {
                    activityDao.observeAllForGrowingSpace(growingSpaceId)
                } else {
                    activityDao.observeActiveForGrowingSpace(growingSpaceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeActivitiesForPlan(
            planId: String,
            includeArchived: Boolean,
        ): Flow<List<Activity>> {
            val source =
                if (includeArchived) {
                    activityDao.observeAllForPlan(planId)
                } else {
                    activityDao.observeActiveForPlan(planId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeActivity(id: String): Flow<Activity?> = activityDao.observeById(id).map { it?.toDomain() }

        override suspend fun getActivity(id: String): Activity? = activityDao.getById(id)?.toDomain()

        override suspend fun insert(activity: Activity) {
            requireGardenExists(activity.gardenId)
            activity.areaId?.let { requireAreaExists(it) }
            activity.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            activity.spatialObjectId?.let { requireSpatialObjectExists(it) }
            activity.plantInstanceId?.let { requirePlantInstanceExists(it) }
            activity.planId?.let { requirePlanExists(it) }
            try {
                activityDao.insert(activity.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "activity",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = activityDao.updateStatus(id, RecordStatus.ARCHIVED)
            if (rows == 0) {
                throw NotFoundError("Activity", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = activityDao.updateStatus(id, RecordStatus.ACTIVE)
            if (rows == 0) {
                throw NotFoundError("Activity", id)
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

        private suspend fun requirePlanExists(planId: String) {
            planDao.getById(planId)
                ?: throw ValidationError(
                    field = "plan_id",
                    reason = "Plan does not exist: $planId",
                )
        }
    }
