package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * SpatialObject.
 *
 * V1_DATABASE_SCHEMA.md §14. DATA_MODEL.md §9.
 *
 * Stable identifier format: "spatialobject_<uuid>".
 *
 * Foreign keys to Garden and Area are RESTRICT: neither parent may
 * be deleted while a spatial object references it
 * (V1_DATABASE_SCHEMA §8). area_id is nullable (B2): an object may
 * exist without belonging to a specific Area.
 *
 * geometry_type and geometry_data are both non-null (A5). The
 * mapper cross-checks them, exactly as it does for GrowingSpace.
 *
 * status is non-null (A6, B6): §14 lists it as required. No IS NULL
 * guard in the DAO, unlike Area (where §12 makes status optional).
 *
 * name is nullable (B4): §14 lists it as optional.
 */
@Entity(
    tableName = "spatial_object",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = AreaEntity::class,
            parentColumns = ["id"],
            childColumns = ["area_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["area_id"]),
    ],
)
data class SpatialObjectEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val object_type: InfrastructureType,
    val geometry_type: GeometryType,
    val geometry_data: String,
    val status: RecordStatus,
    val created_at: Long,
    val updated_at: Long,
    val name: String? = null,
    val description: String? = null,
    val area_id: String? = null,
    val notes: String? = null,
)
