package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Plan — domain model.
 *
 * V1_DATABASE_SCHEMA §40. DATA_MODEL §41.
 *
 * Intended future action. Not an Activity (§27, Invariant 3). A Plan
 * does not itself prove that an activity occurred (Invariant 4).
 *
 * `planType` reuses ActivityType (PL3). A Plan of type `watering`
 * intends a watering; when the user performs it, an Activity of type
 * `watering` is recorded. Reusing the vocabulary keeps the two sides
 * of the model aligned without inventing a `PlanType`.
 *
 * `status` (PlanningStatus) is the plan's lifecycle. `recordStatus`
 * (RecordStatus) is the record's archival state (PL5). Orthogonal.
 *
 * Targets live in the plan_target join table (PL1). §40's
 * single-target fields are omitted.
 *
 * No `name` (PL8). No `completed_at` on the Plan itself (PL11): the
 * completion of a plan is evidenced by linked Activities.
 *
 * No history table.
 */
data class Plan(
    val id: String,
    val gardenId: String,
    val planType: ActivityType,
    val status: PlanningStatus,
    val recordStatus: RecordStatus?,
    val createdAt: Long,
    val updatedAt: Long,
    val plannedStart: KnownDate?,
    val plannedEnd: KnownDate?,
    val quantity: Double?,
    val unit: ActivityQuantityUnit?,
    val priority: PlanningPriority?,
    val notes: String?,
)
