package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.model.garden.PlanTarget
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Plan and its targets.
 *
 * updateStatus changes the Plan's planning lifecycle (PlanningStatus).
 * archive/restore change its RecordStatus (PL5).
 *
 * addTarget / removeTarget / completeTarget manage the plan_target
 * join table. completeTarget sets completed_at on one target, which
 * supports §29's partial-completion requirement.
 */
interface PlanRepository {
    fun observePlansInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Plan>>

    fun observePlan(id: String): Flow<Plan?>

    suspend fun getPlan(id: String): Plan?

    suspend fun insert(plan: Plan)

    suspend fun updateStatus(
        id: String,
        status: PlanningStatus,
        updatedAt: Long,
    )

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )

    fun observeTargetsForPlan(planId: String): Flow<List<PlanTarget>>

    suspend fun addTarget(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
        notes: String?,
    )

    suspend fun removeTarget(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
    )

    suspend fun completeTarget(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
        completedAt: Long,
    )
}
