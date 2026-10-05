package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestSizeCategory

/**
 * Harvest — domain model.
 *
 * V1_DATABASE_SCHEMA §38. DATA_MODEL §36.
 *
 * Produce successfully collected from the garden. Separate from
 * HarvestLoss (§37, §39). The application must not infer one from
 * the other.
 *
 * §38: "A Harvest must be linked to an actual Activity." activityId
 * is required and references a `harvesting` Activity, created in the
 * same transaction.
 *
 * `date` uses KnownDate (HL3): a user who harvested at 9am can
 * record a timestamp; a user who only knows the day records a
 * date-only. The linked Activity's occurred_at normalizes a
 * date-only value to UTC midnight; see HarvestMapper.
 *
 * HL11: no status column. Archival goes through the linked Activity.
 */
data class Harvest(
    val id: String,
    val activityId: String,
    val gardenId: String,
    val date: KnownDate,
    val quantity: Double,
    val unit: ActivityQuantityUnit,
    val createdAt: Long,
    val sizeCategory: HarvestSizeCategory?,
    val plantInstanceId: String?,
    val growingSpaceId: String?,
    val notes: String?,
)
