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
 * occurredAt is required: S3 (resolved by S2 (i)). Activity is only
 * ever an actual event. Planned work is a Plan, not a planned
 * Activity.
 *
 * detail carries the type-specific subtype (S4 (a)). The mapper
 * pairs detail with activityType; a mismatch is a data-integrity
 * error.
 *
 * status is RecordStatus? (S2 (i)), matching every other entity in
 * the codebase. Nullable because §25 lists it as optional.
 *
 * No updatedAt: S7. Event records are immutable once created.
 * Corrections use §62's append-only mechanism, not in-place
 * mutation.
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
    val quantity: Double?,
    val unit: ActivityQuantityUnit?,
    val detail: ActivityDetail,
    val dataOrigin: DataOrigin?,
    val status: RecordStatus?,
    val notes: String?,
)
