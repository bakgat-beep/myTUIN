package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.RotationGroupType

/**
 * RotationGroup.
 *
 * PLANNING_VOCABULARIES §17. Phase 1 step 4e.
 *
 * Stable identifier format: "rotationgroup_<uuid>" (to be generated
 * by a future write path; read-only in this step).
 *
 * `garden_id` is nullable (P41E-1 (c)): null for system groups,
 * non-null for user-defined ones. The mapper enforces the
 * correspondence between group_type and garden_id.
 */
@Entity(
    tableName = "rotation_group",
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
        Index(value = ["group_type"]),
    ],
)
data class RotationGroupEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val group_type: RotationGroupType,
    val status: RecordStatus,
    val created_at: Long,
    val updated_at: Long,
    val garden_id: String? = null,
    val description: String? = null,
)
