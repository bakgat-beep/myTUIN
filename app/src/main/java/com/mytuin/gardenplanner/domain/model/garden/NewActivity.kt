package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin

/**
 * User-settable fields for a new Activity.
 *
 * The use case assigns id, createdAt and status. `occurredAt` is
 * caller-supplied because the event time is part of the record's
 * meaning, not its metadata.
 */
data class NewActivity(
    val gardenId: String,
    val activityType: ActivityType,
    val occurredAt: Long,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val plantInstanceId: String? = null,
    val quantity: Double? = null,
    val unit: ActivityQuantityUnit? = null,
    val detail: ActivityDetail = ActivityDetail.Plain,
    val dataOrigin: DataOrigin? = null,
    val notes: String? = null,
)
