package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind

/**
 * GardenPlantPreference.
 *
 * DEC-042, A176=a. Composite primary key (garden_id, plant_id, kind).
 *
 * garden_id is the leading PK column and satisfies its FK index
 * requirement. plant_id is not leading, so an explicit index is
 * declared for the plant FK.
 */
@Entity(
    tableName = "garden_plant_preference",
    primaryKeys = ["garden_id", "plant_id", "kind"],
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
    ],
    indices = [
        Index(value = ["plant_id"]),
    ],
)
data class GardenPlantPreferenceEntity(
    val garden_id: String,
    val plant_id: String,
    val kind: GardenPlantPreferenceKind,
)