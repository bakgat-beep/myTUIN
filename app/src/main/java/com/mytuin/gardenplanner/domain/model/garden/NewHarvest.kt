package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.HarvestSizeCategory
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit

/**
 * User-settable fields for a new Harvest.
 *
 * The use case assigns id, activityId and createdAt, and writes the
 * linked harvesting Activity in the same transaction.
 */
data class NewHarvest(
    val gardenId: String,
    val date: KnownDate,
    val quantity: Double,
    val unit: QuantityUnit,
    val sizeCategory: HarvestSizeCategory? = null,
    val plantInstanceId: String? = null,
    val growingSpaceId: String? = null,
    val notes: String? = null,
)
