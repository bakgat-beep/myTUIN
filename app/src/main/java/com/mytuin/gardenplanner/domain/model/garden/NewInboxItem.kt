package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.InboxItemRelatedEntityType
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority

/**
 * User-settable fields for a new InboxItem.
 *
 * The use case assigns id, status (defaults to PENDING), createdAt
 * and updatedAt.
 *
 * If relatedEntityType is supplied, relatedEntityId must be too;
 * the mapper enforces the pairing.
 */
data class NewInboxItem(
    val gardenId: String,
    val itemType: InboxItemType,
    val priority: PlanningPriority,
    val title: String? = null,
    val description: String? = null,
    val relatedEntityType: InboxItemRelatedEntityType? = null,
    val relatedEntityId: String? = null,
    val dueAt: Long? = null,
)
