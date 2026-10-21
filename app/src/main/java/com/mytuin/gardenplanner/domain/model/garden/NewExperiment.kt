package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus

/**
 * User-settable fields for a new Experiment.
 *
 * The use case assigns id, status (defaults to DRAFT), createdAt and
 * updatedAt.
 */
data class NewExperiment(
    val gardenId: String,
    val name: String,
    val question: String,
    val status: ExperimentStatus = ExperimentStatus.DRAFT,
    val hypothesis: String? = null,
    val startDate: KnownDate? = null,
    val endDate: KnownDate? = null,
    val notes: String? = null,
)
