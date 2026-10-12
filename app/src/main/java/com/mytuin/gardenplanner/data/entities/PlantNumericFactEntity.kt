package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.mytuin.gardenplanner.domain.vocabulary.PlantNumericFactKind
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit

/**
 * PlantNumericFact.
 *
 * PLANT_VOCABULARIES §41 (numeric fields). Phase 1 step 4c-B.
 *
 * Composite primary key (plant_id, fact_kind). The leading column
 * satisfies the FK index requirement.
 */
@Entity(
    tableName = "plant_numeric_fact",
    primaryKeys = ["plant_id", "fact_kind"],
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
)
data class PlantNumericFactEntity(
    val plant_id: String,
    val fact_kind: PlantNumericFactKind,
    val min_value: Double? = null,
    val max_value: Double? = null,
    val typical_value: Double? = null,
    val unit: QuantityUnit? = null,
    val notes: String? = null,
)
