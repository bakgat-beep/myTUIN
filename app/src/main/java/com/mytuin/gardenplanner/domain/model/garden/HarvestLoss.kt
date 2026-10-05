package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossCause
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossSeverity

/**
 * HarvestLoss — domain model.
 *
 * V1_DATABASE_SCHEMA §39. DATA_MODEL §37.
 *
 * Produce lost rather than successfully harvested. Distinct from
 * Harvest (§39). The application must not infer one from the other.
 *
 * HL2 (b): activityId is optional. When the loss was part of a
 * harvest event, the caller supplies the harvesting Activity's id so
 * the two records can be correlated. When the loss is standalone (a
 * whole crop lost to frost without a harvest), it is null.
 *
 * HL10: no direct FK to Harvest. Correlation, when it exists, is via
 * the shared activityId.
 *
 * HL12: no status column and no archive method. A standalone loss
 * has no Activity to cascade to. Loss records are always returned by
 * queries.
 */
data class HarvestLoss(
    val id: String,
    val gardenId: String,
    val date: KnownDate,
    val quantity: Double,
    val unit: ActivityQuantityUnit,
    val createdAt: Long,
    val activityId: String?,
    val cause: HarvestLossCause?,
    val severity: HarvestLossSeverity?,
    val plantInstanceId: String?,
    val growingSpaceId: String?,
    val notes: String?,
)
