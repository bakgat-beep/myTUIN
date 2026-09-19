package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

/**
 * GrowingSpaceHistory.
 *
 * V1_DATABASE_SCHEMA.md §15. DEC-041.
 *
 * Append-only. Rows are never updated in place.
 *
 * Captures the prior state of a GrowingSpace at the moment it is
 * superseded: geometry, dimensions, and the effective period
 * [valid_from, valid_to). recorded_at is the moment the change
 * occurred; valid_to equals recorded_at because the row is written
 * at the edit that ends its effective period.
 *
 * The first history row for a space has valid_from equal to the
 * space's created_at. Subsequent rows have valid_from equal to the
 * previous row's valid_to (A65=a).
 *
 * No created_at / updated_at. This is an event-like append-only table
 * (V1_DATABASE_SCHEMA §25), not a state record. V1_DATABASE_SCHEMA §6
 * applies "normally" and does not apply here.
 *
 * Foreign key to GrowingSpace is RESTRICT: history may not be orphaned
 * by deletion of the current row. V1_DATABASE_SCHEMA §8, §67.
 */
@Entity(
    tableName = "growing_space_history",
    foreignKeys = [
        ForeignKey(
            entity = GrowingSpaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["growing_space_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["growing_space_id", "valid_from"]),
    ],
)
data class GrowingSpaceHistoryEntity(
    @PrimaryKey
    val id: String,
    val growing_space_id: String,
    val geometry_type: GeometryType? = null,
    val geometry_data: String? = null,
    val length: Double? = null,
    val width: Double? = null,
    val height: Double? = null,
    val diameter: Double? = null,
    val area: Double? = null,
    val volume: Double? = null,
    val valid_from: Long,
    val valid_to: Long,
    val recorded_at: Long,
    val reason: String? = null,
)
