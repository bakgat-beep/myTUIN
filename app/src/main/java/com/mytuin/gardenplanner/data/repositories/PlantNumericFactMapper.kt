package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantNumericFactEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantNumericFact

/**
 * Mapping between PlantNumericFactEntity and PlantNumericFact.
 *
 * Value-shape invariant (P41C-5 (ii)): at least one of min_value,
 * max_value, typical_value must be non-null; if both min_value and
 * max_value are set, min_value <= max_value. Enforced at read time.
 */

fun PlantNumericFactEntity.toDomain(): PlantNumericFact {
    require(min_value != null || max_value != null || typical_value != null) {
        "PlantNumericFact ${plant_id}/${fact_kind} has no value; " +
            "at least one of min_value, max_value, typical_value must be set"
    }
    if (min_value != null && max_value != null) {
        require(min_value <= max_value) {
            "PlantNumericFact ${plant_id}/${fact_kind} has min_value=$min_value > max_value=$max_value"
        }
    }
    return PlantNumericFact(
        plantId = plant_id,
        factKind = fact_kind,
        minValue = min_value,
        maxValue = max_value,
        typicalValue = typical_value,
        unit = unit,
        notes = notes,
    )
}
