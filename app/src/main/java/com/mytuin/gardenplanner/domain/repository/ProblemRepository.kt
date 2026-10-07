package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.model.garden.ProblemObservation
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Problem and its Observation links.
 *
 * updateStatus changes the Problem's own lifecycle (ProblemStatus).
 * archive/restore change its RecordStatus (P2 (a)).
 *
 * linkObservation / unlinkObservation manage the join table.
 * observeLinkedObservations returns the links for a Problem;
 * observeLinkedProblems returns the links for an Observation.
 */
interface ProblemRepository {
    fun observeProblemsInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Problem>>

    fun observeProblemsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Problem>>

    fun observeProblemsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean = false,
    ): Flow<List<Problem>>

    fun observeProblem(id: String): Flow<Problem?>

    suspend fun getProblem(id: String): Problem?

    suspend fun insert(problem: Problem)

    suspend fun updateStatus(
        id: String,
        status: ProblemStatus,
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

    fun observeLinkedObservations(problemId: String): Flow<List<ProblemObservation>>

    fun observeLinkedProblems(observationId: String): Flow<List<ProblemObservation>>

    suspend fun linkObservation(
        problemId: String,
        observationId: String,
        evidenceDirection: ProblemEvidenceDirection?,
        notes: String?,
    )

    suspend fun unlinkObservation(
        problemId: String,
        observationId: String,
    )
}
