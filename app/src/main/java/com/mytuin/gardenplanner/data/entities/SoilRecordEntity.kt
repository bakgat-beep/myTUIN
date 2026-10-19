package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * SoilRecord.
 *
 * V1_DATABASE_SCHEMA §34. Phase 1 step 5.
 *
 * Stable identifier format: "soilrecord_<uuid>".
 *
 * Three foreign keys, all RESTRICT. garden_id is required;
 * area_id and growing_space_id are nullable and mutually exclusive
 * (S5-1 (a)). SQLite cannot express the exclusivity as a CHECK
 * constraint through Room's @Entity, so the mapper enforces it on
 * read, matching the pattern used by PlantNumericFact,
 * PlantTemporalFact and RotationGroup.
 *
 * No soil-characteristic columns. §34 requires characteristics to
 * live in Observations and Measurements.
 *
 * S5-2 (a): no direct FK from Observation or Measurement.
 * S5-4 (a): no cached summary columns.
 */
@Entity(
    tableName = "soil_record",
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
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["area_id"]),
        Index(value = ["growing_space_id"]),
    ],
)
data class SoilRecordEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val status: RecordStatus,
    val created_at: Long,
    val updated_at: Long,
    val area_id: String? = null,
    val growing_space_id: String? = null,
    val name: String? = null,
    val notes: String? = null,
)
