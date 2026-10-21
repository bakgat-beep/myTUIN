package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType

/**
 * ExperimentLink.
 *
 * V1_DATABASE_SCHEMA §49. Phase 1 step 7.
 *
 * Composite primary key (experiment_id, linked_type, linked_id). The
 * leading column satisfies the FK index requirement; a reverse index
 * on (linked_type, linked_id) supports "which experiments use this
 * record" lookups.
 *
 * `linked_id` is polymorphic and carries no FK (E1 (b)).
 */
@Entity(
    tableName = "experiment_link",
    primaryKeys = ["experiment_id", "linked_type", "linked_id"],
    foreignKeys = [
        ForeignKey(
            entity = ExperimentEntity::class,
            parentColumns = ["id"],
            childColumns = ["experiment_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["linked_type", "linked_id"]),
    ],
)
data class ExperimentLinkEntity(
    val experiment_id: String,
    val linked_type: ExperimentLinkType,
    val linked_id: String,
    val notes: String? = null,
)
