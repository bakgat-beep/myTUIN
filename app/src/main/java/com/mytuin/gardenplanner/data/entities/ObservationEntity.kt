package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType

/**
 * Observation.
 *
 * V1_DATABASE_SCHEMA §30. DATA_MODEL §23–§26.
 *
 * Stable identifier format: "observation_<uuid>".
 *
 * S1 (ii): every Observation is linked to an Activity of type
 * `observation`. activity_id is required and references activity
 * with RESTRICT. The Activity is created in the same transaction as
 * the Observation.
 *
 * O5: no status column. Archival goes through the linked Activity.
 * The DAO's default queries join the activity table to filter
 * archived rows.
 *
 * O2 (a): structured_values is an opaque TEXT payload for Step 3b.
 *
 * O1: problem_id is deliberately absent; added when Problem lands.
 */
@Entity(
    tableName = "observation",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = ActivityEntity::class,
            parentColumns = ["id"],
            childColumns = ["activity_id"],
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
        Index(value = ["activity_id"]),
        Index(value = ["observed_at"]),
        Index(value = ["area_id"]),
        Index(value = ["growing_space_id"]),
        Index(value = ["spatial_object_id"]),
        Index(value = ["plant_instance_id"]),
    ],
)
data class ObservationEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val activity_id: String,
    val observed_at: Long,
    val observation_type: ObservationType,
    val confidence: Confidence,
    val created_at: Long,
    val area_id: String? = null,
    val growing_space_id: String? = null,
    val spatial_object_id: String? = null,
    val plant_instance_id: String? = null,
    val structured_values: String? = null,
    val notes: String? = null,
)
