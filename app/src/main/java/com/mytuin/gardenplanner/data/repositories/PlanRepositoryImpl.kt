package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.PlanDao
import com.mytuin.gardenplanner.data.dao.PlanTargetDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.SpatialObjectDao
import com.mytuin.gardenplanner.data.entities.PlanTargetEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.model.garden.PlanTarget
import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * PlanRepositoryImpl.
 *
 * addTarget pre-checks both the Plan and the target entity. Because
 * target_id is untyped (PL2 (a)), the target check dispatches on
 * target_type.
 *
 * addTarget reuses any existing completed_at for the same triple, so
 * re-adding a target does not silently clear its completion.
 */
class PlanRepositoryImpl
    @Inject
    constructor(
        private val planDao: PlanDao,
        private val planTargetDao: PlanTargetDao,
        private val gardenDao: GardenDao,
        private val plantDao: PlantDao,
        private val cultivarDao: CultivarDao,
        private val plantInstanceDao: PlantInstanceDao,
        private val growingSpaceDao: GrowingSpaceDao,
        private val areaDao: AreaDao,
        private val spatialObjectDao: SpatialObjectDao,
    ) : PlanRepository {
        override fun observePlansInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<Plan>> {
            val source =
                if (includeArchived) {
                    planDao.observeAllForGarden(gardenId)
                } else {
                    planDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observePlan(id: String): Flow<Plan?> = planDao.observeById(id).map { it?.toDomain() }

        override suspend fun getPlan(id: String): Plan? = planDao.getById(id)?.toDomain()

        override suspend fun insert(plan: Plan) {
            requireGardenExists(plan.gardenId)
            try {
                planDao.insert(plan.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "plan",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun updateStatus(
            id: String,
            status: PlanningStatus,
            updatedAt: Long,
        ) {
            val rows = planDao.updateStatus(id, status, updatedAt)
            if (rows == 0) {
                throw NotFoundError("Plan", id)
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = planDao.updateRecordStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("Plan", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = planDao.updateRecordStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("Plan", id)
            }
        }

        override fun observeTargetsForPlan(planId: String): Flow<List<PlanTarget>> =
            planTargetDao.observeForPlan(planId).map { rows -> rows.map { it.toDomain() } }

        override suspend fun addTarget(
            planId: String,
            targetType: PlanTargetType,
            targetId: String,
            notes: String?,
        ) {
            requirePlanExists(planId)
            requireTargetExists(targetType, targetId)

            // Preserve any prior completion for the same triple.
            val prior = planTargetDao.get(planId, targetType, targetId)

            planTargetDao.upsert(
                PlanTargetEntity(
                    plan_id = planId,
                    target_type = targetType,
                    target_id = targetId,
                    completed_at = prior?.completed_at,
                    notes = notes,
                ),
            )
        }

        override suspend fun removeTarget(
            planId: String,
            targetType: PlanTargetType,
            targetId: String,
        ) {
            planTargetDao.delete(planId, targetType, targetId)
        }

        override suspend fun completeTarget(
            planId: String,
            targetType: PlanTargetType,
            targetId: String,
            completedAt: Long,
        ) {
            val rows = planTargetDao.markCompleted(planId, targetType, targetId, completedAt)
            if (rows == 0) {
                throw NotFoundError(
                    "PlanTarget",
                    "$planId/$targetType/$targetId",
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

        private suspend fun requirePlanExists(planId: String) {
            planDao.getById(planId)
                ?: throw ValidationError(
                    field = "plan_id",
                    reason = "Plan does not exist: $planId",
                )
        }

        private suspend fun requireTargetExists(
            targetType: PlanTargetType,
            targetId: String,
        ) {
            val exists =
                when (targetType) {
                    PlanTargetType.PLANT -> plantDao.getById(targetId) != null
                    PlanTargetType.CULTIVAR -> cultivarDao.getById(targetId) != null
                    PlanTargetType.PLANT_INSTANCE -> plantInstanceDao.getById(targetId) != null
                    PlanTargetType.GROWING_SPACE -> growingSpaceDao.getById(targetId) != null
                    PlanTargetType.AREA -> areaDao.getById(targetId) != null
                    PlanTargetType.SPATIAL_OBJECT -> spatialObjectDao.getById(targetId) != null
                }
            if (!exists) {
                throw ValidationError(
                    field = "target_id",
                    reason = "$targetType does not exist: $targetId",
                )
            }
        }
    }
