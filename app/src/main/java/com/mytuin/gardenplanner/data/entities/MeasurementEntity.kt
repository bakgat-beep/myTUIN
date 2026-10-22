package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit

/**
 * Measurement.
 *
 * V1_DATABASE_SCHEMA §32. Phase 1 step 8 resolves the M5 deferral:
 * `source_id` is an optional FK to Source, RESTRICT.
 */
@Entity(
    tableName = "measurement",
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
        ForeignKey(
            entity = SourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["source_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["measured_at_epoch_day"]),
        Index(value = ["property"]),
        Index(value = ["area_id"]),
        Index(value = ["growing_space_id"]),
        Index(value = ["spatial_object_id"]),
        Index(value = ["plant_instance_id"]),
        Index(value = ["source_id"]),
    ],
)
data class MeasurementEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val property: MeasurementProperty,
    val value: Double,
    val unit: QuantityUnit,
    val measured_at_epoch_day: Int,
    val confidence: Confidence,
    val created_at: Long,
    val measured_at_time_of_day_millis: Int? = null,
    val area_id: String? = null,
    val growing_space_id: String? = null,
    val spatial_object_id: String? = null,
    val plant_instance_id: String? = null,
    val source_id: String? = null,
    val notes: String? = null,
)
