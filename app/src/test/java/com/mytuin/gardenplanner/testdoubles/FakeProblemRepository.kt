package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.model.garden.ProblemObservation
import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeProblemRepository : ProblemRepository {
    data class StatusCall(
        val id: String,
        val status: ProblemStatus,
        val at: Long,
    )

    data class RecordStatusCall(
        val id: String,
        val recordStatus: RecordStatus,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Problem>>(emptyList())
    private val links = MutableStateFlow<List<ProblemObservation>>(emptyList())
    private val statusCalls = mutableListOf<StatusCall>()
    private val recordStatusCalls = mutableListOf<RecordStatusCall>()

    override fun observeProblemsInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Problem>> =
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

    override fun observeProblemsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean,
    ): Flow<List<Problem>> =
        store.map { rows ->
            rows
                .filter { it.plantInstanceId == plantInstanceId }
                .let { filtered ->
                    if (includeArchived) {
                        filtered
                    } else {
                        filtered.filter { it.recordStatus != RecordStatus.ARCHIVED }
                    }
                }
        }

    override fun observeProblemsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean,
    ): Flow<List<Problem>> =
        store.map { rows ->
            rows
                .filter { it.growingSpaceId == growingSpaceId }
                .let { filtered ->
                    if (includeArchived) {
                        filtered
                    } else {
                        filtered.filter { it.recordStatus != RecordStatus.ARCHIVED }
                    }
                }
        }

    override fun observeProblem(id: String): Flow<Problem?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getProblem(id: String): Problem? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(problem: Problem) {
        store.value = store.value + problem
    }

    override suspend fun updateStatus(
        id: String,
        status: ProblemStatus,
        updatedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Problem", id)
        }
        statusCalls.add(StatusCall(id, status, updatedAt))
        store.value =
            store.value.map { problem ->
                if (problem.id == id) {
                    problem.copy(status = status, updatedAt = updatedAt)
                } else {
                    problem
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

    override fun observeLinkedObservations(problemId: String): Flow<List<ProblemObservation>> =
        links.map { rows -> rows.filter { it.problemId == problemId } }

    override fun observeLinkedProblems(observationId: String): Flow<List<ProblemObservation>> =
        links.map { rows -> rows.filter { it.observationId == observationId } }

    override suspend fun linkObservation(
        problemId: String,
        observationId: String,
        evidenceDirection: ProblemEvidenceDirection?,
        notes: String?,
    ) {
        links.value =
            links.value
                .filterNot { it.problemId == problemId && it.observationId == observationId } +
            ProblemObservation(problemId, observationId, evidenceDirection, notes)
    }

    override suspend fun unlinkObservation(
        problemId: String,
        observationId: String,
    ) {
        links.value = links.value.filterNot { it.problemId == problemId && it.observationId == observationId }
    }

    private fun applyRecordStatus(
        id: String,
        recordStatus: RecordStatus,
        at: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Problem", id)
        }
        recordStatusCalls.add(RecordStatusCall(id, recordStatus, at))
        store.value =
            store.value.map { problem ->
                if (problem.id == id) {
                    problem.copy(recordStatus = recordStatus, updatedAt = at)
                } else {
                    problem
                }
            }
    }

    fun snapshot(): List<Problem> = store.value

    fun linksSnapshot(): List<ProblemObservation> = links.value

    fun statusCalls(): List<StatusCall> = statusCalls.toList()

    fun recordStatusCalls(): List<RecordStatusCall> = recordStatusCalls.toList()
}
