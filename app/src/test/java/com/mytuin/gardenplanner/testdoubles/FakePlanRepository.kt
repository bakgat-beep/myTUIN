package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.model.garden.PlanTarget
import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePlanRepository : PlanRepository {
    data class StatusCall(
        val id: String,
        val status: PlanningStatus,
        val at: Long,
    )

    data class RecordStatusCall(
        val id: String,
        val recordStatus: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Plan>>(emptyList())
    private val targets = MutableStateFlow<List<PlanTarget>>(emptyList())
    private val statusCalls = mutableListOf<StatusCall>()
    private val recordStatusCalls = mutableListOf<RecordStatusCall>()

    override fun observePlansInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Plan>> =
        store.map { rows ->
            rows
                .filter { it.gardenId == gardenId }
                .let { filtered ->
                    if (includeArchived) {
                        filtered
                    } else {
                        filtered.filter { it.recordStatus != RecordStatus.ARCHIVED }
                    }
                }
        }

    override fun observePlan(id: String): Flow<Plan?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getPlan(id: String): Plan? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(plan: Plan) {
        store.value = store.value + plan
    }

    override suspend fun updateStatus(
        id: String,
        status: PlanningStatus,
        updatedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Plan", id)
        }
        statusCalls.add(StatusCall(id, status, updatedAt))
        store.value =
            store.value.map { plan ->
                if (plan.id == id) {
                    plan.copy(status = status, updatedAt = updatedAt)
                } else {
                    plan
                }
            }
    }

    override suspend fun archive(
        id: String,
        archivedAt: Long,
    ) {
        applyRecordStatus(id, RecordStatus.ARCHIVED, archivedAt)
    }

    override suspend fun restore(
        id: String,
        restoredAt: Long,
    ) {
        applyRecordStatus(id, RecordStatus.ACTIVE, restoredAt)
    }

    override fun observeTargetsForPlan(planId: String): Flow<List<PlanTarget>> = targets.map { rows -> rows.filter { it.planId == planId } }

    override suspend fun addTarget(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
        notes: String?,
    ) {
        val prior =
            targets.value.firstOrNull {
                it.planId == planId && it.targetType == targetType && it.targetId == targetId
            }
        targets.value =
            targets.value
                .filterNot {
                    it.planId == planId && it.targetType == targetType && it.targetId == targetId
                } +
            PlanTarget(planId, targetType, targetId, prior?.completedAt, notes)
    }

    override suspend fun removeTarget(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
    ) {
        targets.value =
            targets.value.filterNot {
                it.planId == planId && it.targetType == targetType && it.targetId == targetId
            }
    }

    override suspend fun completeTarget(
        planId: String,
        targetType: PlanTargetType,
        targetId: String,
        completedAt: Long,
    ) {
        val before = targets.value
        val after =
            before.map { target ->
                if (target.planId == planId && target.targetType == targetType && target.targetId == targetId) {
                    target.copy(completedAt = completedAt)
                } else {
                    target
                }
            }
        if (before == after) {
            throw NotFoundError("PlanTarget", "$planId/$targetType/$targetId")
        }
        targets.value = after
    }

    private fun applyRecordStatus(
        id: String,
        recordStatus: RecordStatus,
        at: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Plan", id)
        }
        recordStatusCalls.add(RecordStatusCall(id, recordStatus, at))
        store.value =
            store.value.map { plan ->
                if (plan.id == id) {
                    plan.copy(recordStatus = recordStatus, updatedAt = at)
                } else {
                    plan
                }
            }
    }

    fun snapshot(): List<Plan> = store.value

    fun targetsSnapshot(): List<PlanTarget> = targets.value

    fun statusCalls(): List<StatusCall> = statusCalls.toList()

    fun recordStatusCalls(): List<RecordStatusCall> = recordStatusCalls.toList()
}
