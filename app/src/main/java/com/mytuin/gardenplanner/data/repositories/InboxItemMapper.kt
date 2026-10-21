package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.InboxItemEntity
import com.mytuin.gardenplanner.domain.model.garden.InboxItem

/**
 * Mapping between InboxItemEntity and InboxItem.
 *
 * Invariant: related_entity_type and related_entity_id must both be
 * null or both be non-null. Enforced at read time.
 */

fun InboxItemEntity.toDomain(): InboxItem {
    require((related_entity_type == null) == (related_entity_id == null)) {
        "InboxItem '$id' has inconsistent related_entity fields; " +
            "got type=$related_entity_type, id=$related_entity_id"
    }
    return InboxItem(
        id = id,
        gardenId = garden_id,
        itemType = item_type,
        priority = priority,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
        title = title,
        description = description,
        relatedEntityType = related_entity_type,
        relatedEntityId = related_entity_id,
        dueAt = due_at,
        mutedUntil = muted_until,
    )
}

fun InboxItem.toEntity(): InboxItemEntity =
    InboxItemEntity(
        id = id,
        garden_id = gardenId,
        item_type = itemType,
        priority = priority,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        title = title,
        description = description,
        related_entity_type = relatedEntityType,
        related_entity_id = relatedEntityId,
        due_at = dueAt,
        muted_until = mutedUntil,
    )
