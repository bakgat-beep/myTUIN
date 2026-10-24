package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Experiment
import com.mytuin.gardenplanner.domain.model.garden.ExperimentLink
import com.mytuin.gardenplanner.domain.repository.ExperimentRepository
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeExperimentRepository : ExperimentRepository {
    private val experiments = MutableStateFlow<List<Experiment>>(emptyList())
    private val links = MutableStateFlow<List<ExperimentLink>>(emptyList())

    override fun observeExperimentsInGarden(gardenId: String): Flow<List<Experiment>> =
        experiments.map { rows ->
            rows.filter { it.gardenId == gardenId }.sortedByDescending { it.createdAt }
        }

    override fun observeExperiment(id: String): Flow<Experiment?> = experiments.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getExperiment(id: String): Experiment? = experiments.value.firstOrNull { it.id == id }

    override suspend fun insert(experiment: Experiment) {
        experiments.value = experiments.value + experiment
    }

    override suspend fun updateStatus(
        id: String,
        status: ExperimentStatus,
        updatedAt: Long,
    ) {
        if (experiments.value.none { it.id == id }) {
            throw NotFoundError("Experiment", id)
        }
        experiments.value =
            experiments.value.map { row ->
                if (row.id == id) {
                    row.copy(status = status, updatedAt = updatedAt)
                } else {
                    row
                }
            }
    }

    override fun observeLinksForExperiment(experimentId: String): Flow<List<ExperimentLink>> =
        links.map { rows -> rows.filter { it.experimentId == experimentId } }

    override suspend fun addLink(link: ExperimentLink) {
        links.value =
            links.value
                .filterNot {
                    it.experimentId == link.experimentId &&
                        it.linkedType == link.linkedType &&
                        it.linkedId == link.linkedId
                } + link
    }

    override suspend fun removeLink(
        experimentId: String,
        linkedType: ExperimentLinkType,
        linkedId: String,
    ) {
        links.value =
            links.value.filterNot {
                it.experimentId == experimentId &&
                    it.linkedType == linkedType &&
                    it.linkedId == linkedId
            }
    }

    fun experimentsSnapshot(): List<Experiment> = experiments.value

    fun linksSnapshot(): List<ExperimentLink> = links.value
}
