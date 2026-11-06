package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

/**
 * SpatialObjectHistory.
 *
 * V1_DATABASE_SCHEMA.md §15. DEC-041 / DEC-051.
 *
 * Append-only. Rows are never updated in place.
 *
 * Captures the prior state of a SpatialObject at the moment it is
 * superseded: geometry and the effective period [valid_from,
 * valid_to). recorded_at is the moment the change occurred;
 * valid_to equals recorded_at because the row is written at the
 * edit that ends its effective period.
 *
 * The first history row for an object has valid_from equal to the
 * object's created_at. Subsequent rows have valid_from equal to the
 * previous row's valid_to.
 *
 * geometry_type and geometry_data are non-null, matching the
 * current SpatialObject table (A5). GrowingSpaceHistory has them
 * nullable because growing-space geometry is optional; this
 * difference mirrors the schema faithfully.
 *
 * No created_at / updated_at. This is an event-like append-only
 * table (V1_DATABASE_SCHEMA §25), not a state record.
 *
 * Foreign key to SpatialObject is RESTRICT: history may not be
 * orphaned by deletion of the current row. V1_DATABASE_SCHEMA §8,
 * §67.
 */
@Entity(
    tableName = "spatial_object_history",
    foreignKeys = [
        ForeignKey(
            entity = SpatialObjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["spatial_object_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["spatial_object_id", "valid_from"]),
    ],
)
data class SpatialObjectHistoryEntity(
    @PrimaryKey
    val id: String,
    val spatial_object_id: String,
    val geometry_type: GeometryType,
    val geometry_data: String,
    val valid_from: Long,
    val valid_to: Long,
    val recorded_at: Long,
    val reason: String? = null,
)
