package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlanEntity
import com.mytuin.gardenplanner.data.entities.PlanTargetEntity
import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.model.garden.PlanTarget

fun PlanEntity.toDomain(): Plan =
    Plan(
        id = id,
        gardenId = garden_id,
        planType = plan_type,
        status = status,
        recordStatus = record_status,
        createdAt = created_at,
        updatedAt = updated_at,
        plannedStart = knownDateFromEntity(planned_start_epoch_day, planned_start_time_of_day_millis),
        plannedEnd = knownDateFromEntity(planned_end_epoch_day, planned_end_time_of_day_millis),
        quantity = quantity,
        unit = unit,
        priority = priority,
        notes = notes,
    )

fun Plan.toEntity(): PlanEntity =
    PlanEntity(
        id = id,
        garden_id = gardenId,
        plan_type = planType,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        record_status = recordStatus,
        planned_start_epoch_day = plannedStart?.date?.toEpochDay()?.toInt(),
        planned_start_time_of_day_millis = plannedStart.encodeTime(),
        planned_end_epoch_day = plannedEnd?.date?.toEpochDay()?.toInt(),
        planned_end_time_of_day_millis = plannedEnd.encodeTime(),
        quantity = quantity,
        unit = unit,
        priority = priority,
        notes = notes,
    )

fun PlanTargetEntity.toDomain(): PlanTarget =
    PlanTarget(
        planId = plan_id,
        targetType = target_type,
        targetId = target_id,
        completedAt = completed_at,
        notes = notes,
    )
