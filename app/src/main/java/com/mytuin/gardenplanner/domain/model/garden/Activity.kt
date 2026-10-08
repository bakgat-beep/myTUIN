package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Activity — domain model.
 *
 * V1_DATABASE_SCHEMA §25. DATA_MODEL §32.
 *
 * The core record of something that actually happened. It is not a
 * Plan (§27). It is not evidence that a plan was completed unless
 * the user explicitly recorded the activity.
 *
 * PL9: `planId` optionally links an Activity to the Plan it
 * completes. Per ACTIVITY_VOCABULARIES §29, the completed activity
 * references the originating plan. Nullable because most Activities
 * are not completions of a plan.
 *
 * Invariant 4 protects the semantic: a Plan being `completed` is
 * not itself evidence that an Activity occurred. The presence of
 * this link says the user recorded the completion.
 *
 * occurredAt is required (S3). detail carries the type-specific
 * subtype (S4 (a)). status is RecordStatus? (S2 (i)). No updatedAt
 * (S7).
 */
data class Activity(
    val id: String,
    val gardenId: String,
    val activityType: ActivityType,
    val occurredAt: Long,
    val createdAt: Long,
    val areaId: String?,
    val growingSpaceId: String?,
    val spatialObjectId: String?,
    val plantInstanceId: String?,
    val planId: String?,
    val quantity: Double?,
    val unit: ActivityQuantityUnit?,
    val detail: ActivityDetail,
    val dataOrigin: DataOrigin?,
    val status: RecordStatus?,
    val notes: String?,
)
