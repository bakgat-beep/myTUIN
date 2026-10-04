package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementUnit

/**
 * Measurement — domain model.
 *
 * V1_DATABASE_SCHEMA §32. DATA_MODEL §27.
 *
 * A measured value: pH = 6.7, temperature = 18.4 °C, sunlight = 6.5
 * hours. Distinct from an Observation (§33, Invariant 6) and from a
 * derived estimate (§65).
 *
 * M1 (a): no link to Activity. §32 does not require one, and no
 * `measurement` activity type exists in ACTIVITY_VOCABULARIES §3.1.
 * `measuredAt` serves the timeline purpose on its own.
 *
 * M6: `measuredAt` uses KnownDate, so a date-only reading is stored
 * as date-only and a timestamped reading as a timestamp. The mapper
 * enforces the pair.
 *
 * M7, M8: no information_state, no status, no updated_at, no
 * structured_values. All deliberate.
 */
data class Measurement(
    val id: String,
    val gardenId: String,
    val property: MeasurementProperty,
    val value: Double,
    val unit: MeasurementUnit,
    val measuredAt: KnownDate,
    val confidence: Confidence,
    val createdAt: Long,
    val areaId: String?,
    val growingSpaceId: String?,
    val spatialObjectId: String?,
    val plantInstanceId: String?,
    val notes: String?,
)
