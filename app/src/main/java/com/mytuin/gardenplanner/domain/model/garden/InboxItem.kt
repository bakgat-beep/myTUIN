package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.InboxItemRelatedEntityType
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority

/**
 * InboxItem — domain model.
 *
 * V1_DATABASE_SCHEMA §52. DATA_MODEL §80.
 *
 * User-facing workflow state. Completing or dismissing an InboxItem
 * must not alter the underlying historical record unless the user
 * explicitly performs that action (§52).
 *
 * `priority` reuses PlanningPriority (E7), whose four-value scale
 * applies to any "how important is this work" classification.
 *
 * `status` uses InboxItemStatus (E5 (a)); mute is represented by
 * `mutedUntil`, not by a status.
 *
 * `relatedEntityType` + `relatedEntityId` are optional and
 * polymorphic (E6).
 */
data class InboxItem(
    val id: String,
    val gardenId: String,
    val itemType: InboxItemType,
    val priority: PlanningPriority,
    val status: InboxItemStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val title: String?,
    val description: String?,
    val relatedEntityType: InboxItemRelatedEntityType?,
    val relatedEntityId: String?,
    val dueAt: Long?,
    val mutedUntil: Long?,
)
