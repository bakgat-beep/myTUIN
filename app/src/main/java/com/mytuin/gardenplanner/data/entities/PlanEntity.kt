package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Plan.
 *
 * V1_DATABASE_SCHEMA §40. DATA_MODEL §41.
 *
 * Stable identifier format: "plan_<uuid>".
 *
 * One foreign key to Garden, RESTRICT.
 *
 * `status` (PlanningStatus) and `record_status` (RecordStatus) are
 * orthogonal (PL5). The DAO's default queries filter on record_status.
 *
 * Dates use the (epoch_day, time_of_day_millis) pair (PL6). Both
 * are nullable: a plan may have no dates at all.
 *
 * Targets are in the plan_target table (§41, PL1). No single-target
 * columns here.
 */
@Entity(
    tableName = "plan",
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
        Index(value = ["planned_start_epoch_day"]),
        Index(value = ["planned_end_epoch_day"]),
    ],
)
data class PlanEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val plan_type: ActivityType,
    val status: PlanningStatus,
    val created_at: Long,
    val updated_at: Long,
    val record_status: RecordStatus? = null,
    val planned_start_epoch_day: Int? = null,
    val planned_start_time_of_day_millis: Int? = null,
    val planned_end_epoch_day: Int? = null,
    val planned_end_time_of_day_millis: Int? = null,
    val quantity: Double? = null,
    val unit: ActivityQuantityUnit? = null,
    val priority: PlanningPriority? = null,
    val notes: String? = null,
)
