package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus

/**
 * Experiment.
 *
 * V1_DATABASE_SCHEMA §49. Phase 1 step 7.
 *
 * Stable identifier format: "experiment_<uuid>".
 *
 * One FK to Garden, RESTRICT.
 *
 * `updated_at` is not listed in §49 but is added here, matching the
 * state-record pattern (Problem, Plan, SoilRecord). A status change
 * without a timestamp would leave the change untraceable.
 */
@Entity(
    tableName = "experiment",
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
        Index(value = ["status"]),
    ],
)
data class ExperimentEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val name: String,
    val question: String,
    val status: ExperimentStatus,
    val created_at: Long,
    val updated_at: Long,
    val hypothesis: String? = null,
    val start_date_epoch_day: Int? = null,
    val start_date_time_of_day_millis: Int? = null,
    val end_date_epoch_day: Int? = null,
    val end_date_time_of_day_millis: Int? = null,
    val notes: String? = null,
)
