package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import com.mytuin.gardenplanner.domain.vocabulary.GardenPriority

/**
 * GardenPreference.
 *
 * DEC-042. Composite primary key (garden_id, preference_key). The
 * leading column satisfies the FK index requirement, so no separate
 * index is declared (A190).
 *
 * No created_at / updated_at. Preferences are current-state values,
 * not event records (A178=b). V1_DATABASE_SCHEMA §6 applies
 * "normally"; these are the exception.
 */
@Entity(
    tableName = "garden_preference",
    primaryKeys = ["garden_id", "preference_key"],
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
)
data class GardenPreferenceEntity(
    val garden_id: String,
    val preference_key: GardenPreferenceKey,
    val priority: GardenPriority,
)