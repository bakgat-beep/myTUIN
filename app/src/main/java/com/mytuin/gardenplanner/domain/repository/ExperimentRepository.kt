package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Experiment
import com.mytuin.gardenplanner.domain.model.garden.ExperimentLink
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Experiment and ExperimentLink.
 *
 * updateStatus changes the Experiment's lifecycle (ExperimentStatus,
 * §18). No archive/restore: the lifecycle states cover retirement
 * (cancelled, abandoned).
 *
 * Link management: addLink / removeLink / observeLinksForExperiment.
 * The polymorphic linkedId is not pre-checked (E1 (b)); the caller
 * is responsible for referring to an existing record.
 */
interface ExperimentRepository {
    fun observeExperimentsInGarden(gardenId: String): Flow<List<Experiment>>

    fun observeExperiment(id: String): Flow<Experiment?>

    suspend fun getExperiment(id: String): Experiment?

    suspend fun insert(experiment: Experiment)

    suspend fun updateStatus(
        id: String,
        status: ExperimentStatus,
        updatedAt: Long,
    )

    fun observeLinksForExperiment(experimentId: String): Flow<List<ExperimentLink>>

    suspend fun addLink(link: ExperimentLink)

    suspend fun removeLink(
        experimentId: String,
        linkedType: ExperimentLinkType,
        linkedId: String,
    )
}
