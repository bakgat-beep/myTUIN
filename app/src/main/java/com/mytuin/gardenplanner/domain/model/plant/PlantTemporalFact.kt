package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantTemporalFactKind
import com.mytuin.gardenplanner.domain.vocabulary.Season

/**
 * A recurring temporal fact attached to a Plant.
 *
 * PLANT_VOCABULARIES §41 (temporal fields). Stored in the
 * plant_temporal_fact table. One row per (plant, kind).
 *
 * Value shape (P41C-6 (ii)): a recurring annual window expressed as
 * start month/day and end month/day. No year. Months in 1..12, days
 * in 1..31. Days are optional even when months are set.
 *
 * `hemisphere` and `geographicScope` (P41C-3 (b)) provide optional
 * context. A generic "typical temperate climate" row has both null.
 * If a requirement surfaces for per-hemisphere values, the PK is
 * extended in a later migration.
 *
 * `season` (CORE_VOCABULARIES §9) classifies the window conceptually
 * when the user prefers that representation to explicit dates.
 */
data class PlantTemporalFact(
    val plantId: String,
    val factKind: PlantTemporalFactKind,
    val startMonth: Int?,
    val startDay: Int?,
    val endMonth: Int?,
    val endDay: Int?,
    val season: Season?,
    val hemisphere: Hemisphere?,
    val geographicScope: GeographicScope?,
    val notes: String?,
)
