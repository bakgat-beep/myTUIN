package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType

/**
 * PlantInstanceHistory.
 *
 * DEC-041. Append-only. Rows are never updated in place.
 *
 * Captures the prior location of a PlantInstance at the moment it is
 * superseded. Location is composite: growing_space_id,
 * spatial_object_id, geometry_type + geometry_data.
 *
 * H3: the spatial-context columns carry no foreign keys. They are
 * historical snapshot values, not live references. If the referenced
 * GrowingSpace or SpatialObject is later archived, the history row
 * remains valid and readable. Only the owning PlantInstance is
 * referenced, and with RESTRICT: history may not be orphaned by
 * deletion of the current row.
 *
 * The first history row for a plant instance has valid_from equal to
 * the instance's created_at. Subsequent rows have valid_from equal
 * to the previous row's valid_to. recorded_at equals valid_to: the
 * row is written at the edit that ends its effective period.
 *
 * No created_at / updated_at. This is an event-like append-only table
 * (V1_DATABASE_SCHEMA §25), not a state record.
 */
@Entity(
    tableName = "plant_instance_history",
    foreignKeys = [
        ForeignKey(
            entity = PlantInstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_instance_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["plant_instance_id", "valid_from"]),
    ],
)
data class PlantInstanceHistoryEntity(
    @PrimaryKey
    val id: String,
    val plant_instance_id: String,
    val valid_from: Long,
    val valid_to: Long,
    val recorded_at: Long,
    val growing_space_id: String? = null,
    val spatial_object_id: String? = null,
    val geometry_type: GeometryType? = null,
    val geometry_data: String? = null,
    val reason: String? = null,
)
