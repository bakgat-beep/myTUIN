package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.AreaDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.data.dao.ObservationDao
import com.mytuin.gardenplanner.data.dao.PlantInstanceDao
import com.mytuin.gardenplanner.data.dao.ProblemDao
import com.mytuin.gardenplanner.data.dao.ProblemObservationDao
import com.mytuin.gardenplanner.data.entities.ProblemObservationEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.model.garden.ProblemObservation
import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Problem has four foreign keys on insert, so a bare
 * SQLiteConstraintException cannot say which one failed. This
 * implementation pre-checks each optional parent, following the
 * established pattern.
 *
 * The Observation link methods pre-check both parents and use
 * REPLACE for upsert, so re-linking the same pair overwrites the
 * evidence direction without raising a constraint error.
 */
class ProblemRepositoryImpl
    @Inject
    constructor(
        private val problemDao: ProblemDao,
        private val problemObservationDao: ProblemObservationDao,
        private val gardenDao: GardenDao,
        private val areaDao: AreaDao,
        private val growingSpaceDao: GrowingSpaceDao,
        private val plantInstanceDao: PlantInstanceDao,
        private val observationDao: ObservationDao,
    ) : ProblemRepository {
        override fun observeProblemsInGarden(
            gardenId: String,
            includeArchived: Boolean,
        ): Flow<List<Problem>> {
            val source =
                if (includeArchived) {
                    problemDao.observeAllForGarden(gardenId)
                } else {
                    problemDao.observeActiveForGarden(gardenId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeProblemsForPlantInstance(
            plantInstanceId: String,
            includeArchived: Boolean,
        ): Flow<List<Problem>> {
            val source =
                if (includeArchived) {
                    problemDao.observeAllForPlantInstance(plantInstanceId)
                } else {
                    problemDao.observeActiveForPlantInstance(plantInstanceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeProblemsInGrowingSpace(
            growingSpaceId: String,
            includeArchived: Boolean,
        ): Flow<List<Problem>> {
            val source =
                if (includeArchived) {
                    problemDao.observeAllForGrowingSpace(growingSpaceId)
                } else {
                    problemDao.observeActiveForGrowingSpace(growingSpaceId)
                }
            return source.map { rows -> rows.map { it.toDomain() } }
        }

        override fun observeProblem(id: String): Flow<Problem?> = problemDao.observeById(id).map { it?.toDomain() }

        override suspend fun getProblem(id: String): Problem? = problemDao.getById(id)?.toDomain()

        override suspend fun insert(problem: Problem) {
            requireGardenExists(problem.gardenId)
            problem.areaId?.let { requireAreaExists(it) }
            problem.growingSpaceId?.let { requireGrowingSpaceExists(it) }
            problem.plantInstanceId?.let { requirePlantInstanceExists(it) }
            try {
                problemDao.insert(problem.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "problem",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun updateStatus(
            id: String,
            status: ProblemStatus,
            updatedAt: Long,
        ) {
            val rows = problemDao.updateStatus(id, status, updatedAt)
            if (rows == 0) {
                throw NotFoundError("Problem", id)
            }
        }

        override suspend fun archive(
            id: String,
            archivedAt: Long,
        ) {
            val rows = problemDao.updateRecordStatus(id, RecordStatus.ARCHIVED, archivedAt)
            if (rows == 0) {
                throw NotFoundError("Problem", id)
            }
        }

        override suspend fun restore(
            id: String,
            restoredAt: Long,
        ) {
            val rows = problemDao.updateRecordStatus(id, RecordStatus.ACTIVE, restoredAt)
            if (rows == 0) {
                throw NotFoundError("Problem", id)
            }
        }

        override fun observeLinkedObservations(problemId: String): Flow<List<ProblemObservation>> =
            problemObservationDao
                .observeForProblem(problemId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeLinkedProblems(observationId: String): Flow<List<ProblemObservation>> =
            problemObservationDao
                .observeForObservation(observationId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun linkObservation(
            problemId: String,
            observationId: String,
            evidenceDirection: ProblemEvidenceDirection?,
            notes: String?,
        ) {
            requireProblemExists(problemId)
            requireObservationExists(observationId)
            problemObservationDao.upsert(
                ProblemObservationEntity(
                    problem_id = problemId,
                    observation_id = observationId,
                    evidence_direction = evidenceDirection,
                    notes = notes,
                ),
            )
        }

        override suspend fun unlinkObservation(
            problemId: String,
            observationId: String,
        ) {
            problemObservationDao.delete(problemId, observationId)
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

        private suspend fun requirePlantInstanceExists(plantInstanceId: String) {
            plantInstanceDao.getById(plantInstanceId)
                ?: throw ValidationError(
                    field = "plant_instance_id",
                    reason = "PlantInstance does not exist: $plantInstanceId",
                )
        }

        private suspend fun requireProblemExists(problemId: String) {
            problemDao.getById(problemId)
                ?: throw ValidationError(
                    field = "problem_id",
                    reason = "Problem does not exist: $problemId",
                )
        }

        private suspend fun requireObservationExists(observationId: String) {
            observationDao.getById(observationId)
                ?: throw ValidationError(
                    field = "observation_id",
                    reason = "Observation does not exist: $observationId",
                )
        }
    }
