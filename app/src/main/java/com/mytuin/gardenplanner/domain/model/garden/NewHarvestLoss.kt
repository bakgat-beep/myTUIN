package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossCause
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossSeverity

/**
 * User-settable fields for a new HarvestLoss.
 *
 * The use case assigns id and createdAt. activityId is optional; if
 * supplied, the referenced Activity must exist.
 */
data class NewHarvestLoss(
    val gardenId: String,
    val date: KnownDate,
    val quantity: Double,
    val unit: ActivityQuantityUnit,
    val activityId: String? = null,
    val cause: HarvestLossCause? = null,
    val severity: HarvestLossSeverity? = null,
    val plantInstanceId: String? = null,
    val growingSpaceId: String? = null,
    val notes: String? = null,
)
