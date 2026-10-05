package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossCause
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossSeverity

/**
 * HarvestLoss.
 *
 * V1_DATABASE_SCHEMA §39. DATA_MODEL §37.
 *
 * Stable identifier format: "harvestloss_<uuid>".
 *
 * HL2 (b): activity_id is nullable. When set, it references an
 * existing Activity with RESTRICT; the repository does not create or
 * modify the Activity.
 *
 * HL12: no status column, no archive. The DAO's queries are always
 * unfiltered.
 *
 * HL10: no FK to harvest. Correlation is via activity_id.
 *
 * Four foreign keys, all RESTRICT. Only garden is required.
 */
@Entity(
    tableName = "harvest_loss",
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
            entity = PlantInstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_instance_id"],
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
        Index(value = ["activity_id"]),
        Index(value = ["date_epoch_day"]),
        Index(value = ["plant_instance_id"]),
        Index(value = ["growing_space_id"]),
    ],
)
data class HarvestLossEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val date_epoch_day: Int,
    val quantity: Double,
    val unit: ActivityQuantityUnit,
    val created_at: Long,
    val activity_id: String? = null,
    val date_time_of_day_millis: Int? = null,
    val cause: HarvestLossCause? = null,
    val severity: HarvestLossSeverity? = null,
    val plant_instance_id: String? = null,
    val growing_space_id: String? = null,
    val notes: String? = null,
)
