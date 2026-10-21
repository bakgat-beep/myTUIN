package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType

/**
 * A link from an Experiment to one of its records.
 *
 * V1_DATABASE_SCHEMA §49. Composite identity
 * (experimentId, linkedType, linkedId). The linkedId is
 * polymorphic and has no FK (E1 (b)); the caller is responsible for
 * referring to an existing record.
 */
data class ExperimentLink(
    val experimentId: String,
    val linkedType: ExperimentLinkType,
    val linkedId: String,
    val notes: String?,
)
