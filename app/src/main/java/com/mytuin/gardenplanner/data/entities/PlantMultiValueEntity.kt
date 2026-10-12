package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind

/**
 * PlantMultiValue.
 *
 * PLANT_VOCABULARIES §41 (multi-value fields). Phase 1 step 4b.
 *
 * Composite primary key (plant_id, kind, value_id). The leading
 * column satisfies the FK index requirement; a reverse index on
 * (kind, value_id) supports "all plants with role X" queries.
 *
 * `value_id` is untyped (String) and carries no foreign key
 * (P41B-4). The vocabulary it belongs to is determined by `kind`;
 * the mapper validates the pair at read time.
 *
 * No id, no created_at, no status. Consistent with plan_target and
 * problem_observation.
 */
@Entity(
    tableName = "plant_multi_value",
    primaryKeys = ["plant_id", "kind", "value_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["kind", "value_id"]),
    ],
)
data class PlantMultiValueEntity(
    val plant_id: String,
    val kind: PlantMultiValueKind,
    val value_id: String,
    val notes: String? = null,
)
