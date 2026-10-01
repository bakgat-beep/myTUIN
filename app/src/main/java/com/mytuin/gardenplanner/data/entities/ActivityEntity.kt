package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin
import com.mytuin.gardenplanner.domain.vocabulary.FeedingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PlantingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PruningMethod
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.SoilWorkMethod
import com.mytuin.gardenplanner.domain.vocabulary.WateringMethod

/**
 * Activity.
 *
 * V1_DATABASE_SCHEMA §25. DATA_MODEL §32.
 *
 * Stable identifier format: "activity_<uuid>".
 *
 * Five foreign keys, all RESTRICT. All but garden are nullable.
 *
 * occurred_at is required (S3): Activity is only ever an actual
 * event. Planned work is a Plan.
 *
 * No updated_at (S7): event records are immutable once created.
 *
 * Subtype columns (S4 (a)): five nullable enum columns, of which at
 * most one may be set, and only the one matching activity_type.
 * The mapper enforces this. `intervention_type` is deliberately
 * absent (deferred until Problem lands).
 *
 * quantity and unit are paired: both null or both non-null. The
 * mapper enforces this.
 */
@Entity(
    tableName = "activity",
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
        ForeignKey(
            entity = PlantInstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_instance_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["occurred_at"]),
        Index(value = ["area_id"]),
        Index(value = ["growing_space_id"]),
        Index(value = ["spatial_object_id"]),
        Index(value = ["plant_instance_id"]),
    ],
)
data class ActivityEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val activity_type: ActivityType,
    val occurred_at: Long,
    val created_at: Long,
    val area_id: String? = null,
    val growing_space_id: String? = null,
    val spatial_object_id: String? = null,
    val plant_instance_id: String? = null,
    val quantity: Double? = null,
    val unit: ActivityQuantityUnit? = null,
    val planting_method: PlantingMethod? = null,
    val watering_method: WateringMethod? = null,
    val feeding_method: FeedingMethod? = null,
    val pruning_method: PruningMethod? = null,
    val soil_work_method: SoilWorkMethod? = null,
    val data_origin: DataOrigin? = null,
    val status: RecordStatus? = null,
    val notes: String? = null,
)
