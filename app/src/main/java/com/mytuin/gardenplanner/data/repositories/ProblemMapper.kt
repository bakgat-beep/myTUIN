package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.ProblemEntity
import com.mytuin.gardenplanner.data.entities.ProblemObservationEntity
import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.model.garden.ProblemObservation

fun ProblemEntity.toDomain(): Problem =
    Problem(
        id = id,
        gardenId = garden_id,
        name = name,
        problemType = problem_type,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
        severity = severity,
        confidence = confidence,
        recordStatus = record_status,
        description = description,
        areaId = area_id,
        growingSpaceId = growing_space_id,
        plantInstanceId = plant_instance_id,
        notes = notes,
    )

fun Problem.toEntity(): ProblemEntity =
    ProblemEntity(
        id = id,
        garden_id = gardenId,
        name = name,
        problem_type = problemType,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        severity = severity,
        confidence = confidence,
        record_status = recordStatus,
        description = description,
        area_id = areaId,
        growing_space_id = growingSpaceId,
        plant_instance_id = plantInstanceId,
        notes = notes,
    )

fun ProblemObservationEntity.toDomain(): ProblemObservation =
    ProblemObservation(
        problemId = problem_id,
        observationId = observation_id,
        evidenceDirection = evidence_direction,
        notes = notes,
    )
