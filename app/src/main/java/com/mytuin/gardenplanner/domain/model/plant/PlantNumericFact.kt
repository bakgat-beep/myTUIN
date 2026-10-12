package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.PlantNumericFactKind
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit

/**
 * A numeric fact attached to a Plant.
 *
 * PLANT_VOCABULARIES §41 (numeric fields). Stored in the
 * plant_numeric_fact table. One row per (plant, kind).
 *
 * Value shape (P41C-5 (ii)): minValue, maxValue and typicalValue
 * are all nullable. At least one must be non-null. If both minValue
 * and maxValue are set, minValue <= maxValue. The mapper enforces
 * both invariants.
 *
 * `unit` is a QuantityUnit id. Nullable, because some numeric facts
 * are dimensionless or count-based.
 */
data class PlantNumericFact(
    val plantId: String,
    val factKind: PlantNumericFactKind,
    val minValue: Double?,
    val maxValue: Double?,
    val typicalValue: Double?,
    val unit: QuantityUnit?,
    val notes: String?,
)
