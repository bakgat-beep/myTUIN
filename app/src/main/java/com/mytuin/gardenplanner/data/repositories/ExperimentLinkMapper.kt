package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.ExperimentLinkEntity
import com.mytuin.gardenplanner.domain.model.garden.ExperimentLink

fun ExperimentLinkEntity.toDomain(): ExperimentLink =
    ExperimentLink(
        experimentId = experiment_id,
        linkedType = linked_type,
        linkedId = linked_id,
        notes = notes,
    )

fun ExperimentLink.toEntity(): ExperimentLinkEntity =
    ExperimentLinkEntity(
        experiment_id = experimentId,
        linked_type = linkedType,
        linked_id = linkedId,
        notes = notes,
    )
