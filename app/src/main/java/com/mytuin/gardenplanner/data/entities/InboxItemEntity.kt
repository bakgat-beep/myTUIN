package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemRelatedEntityType
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority

/**
 * InboxItem.
 *
 * V1_DATABASE_SCHEMA §52. Phase 1 step 7.
 *
 * Stable identifier format: "inboxitem_<uuid>".
 *
 * One FK to Garden, RESTRICT. `related_entity_id` is polymorphic
 * and carries no FK (E6).
 *
 * Mute is represented by `muted_until`, not by a status (§53).
 */
@Entity(
    tableName = "inbox_item",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["status"]),
        Index(value = ["priority"]),
        Index(value = ["due_at"]),
        Index(value = ["related_entity_type", "related_entity_id"]),
    ],
)
data class InboxItemEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val item_type: InboxItemType,
    val priority: PlanningPriority,
    val status: InboxItemStatus,
    val created_at: Long,
    val updated_at: Long,
    val title: String? = null,
    val description: String? = null,
    val related_entity_type: InboxItemRelatedEntityType? = null,
    val related_entity_id: String? = null,
    val due_at: Long? = null,
    val muted_until: Long? = null,
)
