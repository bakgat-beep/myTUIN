package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.ExperimentEntity
import com.mytuin.gardenplanner.domain.model.garden.Experiment

fun ExperimentEntity.toDomain(): Experiment =
    Experiment(
        id = id,
        gardenId = garden_id,
        name = name,
        question = question,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
        hypothesis = hypothesis,
        startDate = knownDateFromEntity(start_date_epoch_day, start_date_time_of_day_millis),
        endDate = knownDateFromEntity(end_date_epoch_day, end_date_time_of_day_millis),
        notes = notes,
    )

fun Experiment.toEntity(): ExperimentEntity =
    ExperimentEntity(
        id = id,
        garden_id = gardenId,
        name = name,
        question = question,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        hypothesis = hypothesis,
        start_date_epoch_day = startDate?.date?.toEpochDay()?.toInt(),
        start_date_time_of_day_millis = startDate.encodeTime(),
        end_date_epoch_day = endDate?.date?.toEpochDay()?.toInt(),
        end_date_time_of_day_millis = endDate.encodeTime(),
        notes = notes,
    )
