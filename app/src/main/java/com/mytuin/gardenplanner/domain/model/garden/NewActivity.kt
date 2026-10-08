package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin

/**
 * User-settable fields for a new Activity.
 *
 * The use case assigns id, createdAt and status. `occurredAt` is
 * caller-supplied. `planId` (PL9) optionally links this Activity to
 * the Plan it completes.
 */
data class NewActivity(
    val gardenId: String,
    val activityType: ActivityType,
    val occurredAt: Long,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val plantInstanceId: String? = null,
    val planId: String? = null,
    val quantity: Double? = null,
    val unit: ActivityQuantityUnit? = null,
    val detail: ActivityDetail = ActivityDetail.Plain,
    val dataOrigin: DataOrigin? = null,
    val notes: String? = null,
)
