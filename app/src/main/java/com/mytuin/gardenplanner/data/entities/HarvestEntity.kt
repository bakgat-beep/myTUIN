package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.HarvestSizeCategory

/**
 * Harvest.
 *
 * V1_DATABASE_SCHEMA §38. DATA_MODEL §36.
 *
 * Stable identifier format: "harvest_<uuid>".
 *
 * §38: "A Harvest must be linked to an actual Activity." activity_id
 * is required and references activity with RESTRICT. The Activity is
 * created in the same transaction.
 *
 * HL3: date is stored as an (epoch_day, time_of_day_millis) pair.
 * date_epoch_day is required; date_time_of_day_millis is nullable.
 *
 * HL11: no status column. The DAO's default queries join the
 * activity table to filter archived rows.
 */
@Entity(
    tableName = "harvest",
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
data class HarvestEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val activity_id: String,
    val date_epoch_day: Int,
    val quantity: Double,
    val unit: ActivityQuantityUnit,
    val created_at: Long,
    val date_time_of_day_millis: Int? = null,
    val size_category: HarvestSizeCategory? = null,
    val plant_instance_id: String? = null,
    val growing_space_id: String? = null,
    val notes: String? = null,
)
