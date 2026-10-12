package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantTemporalFactEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantTemporalFact

/**
 * Mapping between PlantTemporalFactEntity and PlantTemporalFact.
 *
 * Validation (P41C-6 (ii)):
 *   - if start_day is set, start_month must be set
 *   - if end_day is set, end_month must be set
 *   - months, when set, in 1..12
 *   - days, when set, in 1..31
 */

fun PlantTemporalFactEntity.toDomain(): PlantTemporalFact {
    if (start_day != null) {
        require(start_month != null) {
            "PlantTemporalFact ${plant_id}/${fact_kind} has start_day but no start_month"
        }
    }
    if (end_day != null) {
        require(end_month != null) {
            "PlantTemporalFact ${plant_id}/${fact_kind} has end_day but no end_month"
        }
    }
    start_month?.let {
        require(it in 1..12) {
            "PlantTemporalFact ${plant_id}/${fact_kind} has start_month=$it out of 1..12"
        }
    }
    end_month?.let {
        require(it in 1..12) {
            "PlantTemporalFact ${plant_id}/${fact_kind} has end_month=$it out of 1..12"
        }
    }
    start_day?.let {
        require(it in 1..31) {
            "PlantTemporalFact ${plant_id}/${fact_kind} has start_day=$it out of 1..31"
        }
    }
    end_day?.let {
        require(it in 1..31) {
            "PlantTemporalFact ${plant_id}/${fact_kind} has end_day=$it out of 1..31"
        }
    }
    return PlantTemporalFact(
        plantId = plant_id,
        factKind = fact_kind,
        startMonth = start_month,
        startDay = start_day,
        endMonth = end_month,
        endDay = end_day,
        season = season,
        hemisphere = hemisphere,
        geographicScope = geographic_scope,
        notes = notes,
    )
}
