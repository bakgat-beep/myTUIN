package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantTemporalFactKind
import com.mytuin.gardenplanner.domain.vocabulary.Season

/**
 * PlantTemporalFact.
 *
 * PLANT_VOCABULARIES §41 (temporal fields). Phase 1 step 4c-B.
 *
 * Composite primary key (plant_id, fact_kind). The leading column
 * satisfies the FK index requirement.
 *
 * Recurring annual window (P41C-6 (ii)): start_month, start_day,
 * end_month, end_day. Months in 1..12, days in 1..31. All nullable;
 * the mapper enforces that if start_day is set, start_month must be
 * set (and same for end).
 *
 * Optional context columns (P41C-3 (b)): hemisphere, geographic_scope.
 */
@Entity(
    tableName = "plant_temporal_fact",
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
data class PlantTemporalFactEntity(
    val plant_id: String,
    val fact_kind: PlantTemporalFactKind,
    val start_month: Int? = null,
    val start_day: Int? = null,
    val end_month: Int? = null,
    val end_day: Int? = null,
    val season: Season? = null,
    val hemisphere: Hemisphere? = null,
    val geographic_scope: GeographicScope? = null,
    val notes: String? = null,
)
