package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * PlantInstance.
 *
 * V1_DATABASE_SCHEMA §19. DATA_MODEL §15–§18.
 *
 * Stable identifier format: "plantinstance_<uuid>".
 *
 * Five foreign keys, all RESTRICT: garden, plant, cultivar, growing
 * space, spatial object. All but garden and plant are nullable.
 *
 * The four date pairs (§7 of the schema: known-date vs known-timestamp)
 * are stored as two columns each:
 *
 *   X_epoch_day: Int?               — days since 1970-01-01
 *   X_time_of_day_millis: Int?      — millis since midnight
 *
 * Invariant: X_time_of_day_millis non-null implies X_epoch_day
 * non-null. Enforced by PlantInstanceMapper, not by SQLite.
 *
 * geometry_type and geometry_data follow the paired pattern used by
 * growing_space. Both nullable.
 *
 * `lifecycle` and `planting_stock` are both nullable. Nullable means
 * "not recorded"; the `unknown` vocabulary value, where present,
 * means "recorded as unknown" (CORE_VOCABULARIES §4). Only
 * PlantingStockType has `unknown`.
 */
@Entity(
    tableName = "plant_instance",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = CultivarEntity::class,
            parentColumns = ["id"],
            childColumns = ["cultivar_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = GrowingSpaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["growing_space_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = SpatialObjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["spatial_object_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["plant_id"]),
        Index(value = ["cultivar_id"]),
        Index(value = ["growing_space_id"]),
        Index(value = ["spatial_object_id"]),
    ],
)
data class PlantInstanceEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val plant_id: String,
    val status: RecordStatus,
    val created_at: Long,
    val updated_at: Long,
    val cultivar_id: String? = null,
    val growing_space_id: String? = null,
    val spatial_object_id: String? = null,
    val name: String? = null,
    val quantity: Int? = null,
    val planned_date_epoch_day: Int? = null,
    val planned_date_time_of_day_millis: Int? = null,
    val planted_date_epoch_day: Int? = null,
    val planted_date_time_of_day_millis: Int? = null,
    val expected_end_date_epoch_day: Int? = null,
    val expected_end_date_time_of_day_millis: Int? = null,
    val removed_date_epoch_day: Int? = null,
    val removed_date_time_of_day_millis: Int? = null,
    val planting_stock: PlantingStockType? = null,
    val lifecycle: PlantInstanceLifecycle? = null,
    val geometry_type: GeometryType? = null,
    val geometry_data: String? = null,
    val notes: String? = null,
)
