package com.mytuin.gardenplanner.data.repositories

import android.database.sqlite.SQLiteConstraintException
import com.mytuin.gardenplanner.data.dao.ExperimentDao
import com.mytuin.gardenplanner.data.dao.ExperimentLinkDao
import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Experiment
import com.mytuin.gardenplanner.domain.model.garden.ExperimentLink
import com.mytuin.gardenplanner.domain.repository.ExperimentRepository
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExperimentRepositoryImpl
    @Inject
    constructor(
        private val experimentDao: ExperimentDao,
        private val experimentLinkDao: ExperimentLinkDao,
        private val gardenDao: GardenDao,
    ) : ExperimentRepository {
        override fun observeExperimentsInGarden(gardenId: String): Flow<List<Experiment>> =
            experimentDao
                .observeForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeExperiment(id: String): Flow<Experiment?> = experimentDao.observeById(id).map { it?.toDomain() }

        override suspend fun getExperiment(id: String): Experiment? = experimentDao.getById(id)?.toDomain()

        override suspend fun insert(experiment: Experiment) {
            gardenDao.getById(experiment.gardenId)
                ?: throw ValidationError(
                    field = "garden_id",
                    reason = "Garden does not exist: ${experiment.gardenId}",
                )
            try {
                experimentDao.insert(experiment.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "experiment",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun updateStatus(
            id: String,
            status: ExperimentStatus,
            updatedAt: Long,
        ) {
            val rows = experimentDao.updateStatus(id, status, updatedAt)
            if (rows == 0) {
                throw NotFoundError("Experiment", id)
            }
        }

        override fun observeLinksForExperiment(experimentId: String): Flow<List<ExperimentLink>> =
            experimentLinkDao
                .observeForExperiment(experimentId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun addLink(link: ExperimentLink) {
            // FK on experiment_id is enforced by SQLite; the linked_id
            // is polymorphic and not pre-checked (E1 (b)).
            try {
                experimentLinkDao.upsert(link.toEntity())
            } catch (e: SQLiteConstraintException) {
                throw ValidationError(
                    field = "experiment_id",
                    reason = e.message ?: "constraint violation",
                    cause = e,
                )
            }
        }

        override suspend fun removeLink(
            experimentId: String,
            linkedType: ExperimentLinkType,
            linkedId: String,
        ) {
            experimentLinkDao.delete(experimentId, linkedType, linkedId)
        }
    }
