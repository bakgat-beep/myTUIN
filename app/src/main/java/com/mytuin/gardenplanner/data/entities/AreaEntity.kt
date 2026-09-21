package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Area.
 *
 * V1_DATABASE_SCHEMA.md §12. DATA_MODEL.md §7.
 *
 * Stable identifier format: "area_<uuid>".
 *
 * Foreign key to Garden is RESTRICT: a Garden may not be deleted
 * while Areas reference it (V1_DATABASE_SCHEMA §8).
 *
 * geometry_type and geometry_data are paired, both null or both set.
 * The mapper enforces this, exactly as for GrowingSpace. Same GeoJSON
 * representation, same local-metre frame (A50b=(i)).
 *
 * status is nullable (A6): §12 lists it as optional. A null status
 * means "no lifecycle state recorded", distinct from "recorded as
 * unknown" (CORE_VOCABULARIES §4). DAO filters treat null as
 * not-archived.
 */
@Entity(
    tableName = "area",
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
    ],
)
data class AreaEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val name: String,
    val area_type: AreaType,
    val created_at: Long,
    val updated_at: Long,
    val description: String? = null,
    val geometry_type: GeometryType? = null,
    val geometry_data: String? = null,
    val status: RecordStatus? = null,
)
