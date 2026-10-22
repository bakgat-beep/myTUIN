package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit

/**
 * Measurement — domain model.
 *
 * V1_DATABASE_SCHEMA §32. DATA_MODEL §27.
 *
 * Step 8 resolves the M5 deferral: `sourceId` links a Measurement to
 * the Source it was derived from. Nullable; most user-entered
 * measurements have no source.
 */
data class Measurement(
    val id: String,
    val gardenId: String,
    val property: MeasurementProperty,
    val value: Double,
    val unit: QuantityUnit,
    val measuredAt: KnownDate,
    val confidence: Confidence,
    val createdAt: Long,
    val areaId: String?,
    val growingSpaceId: String?,
    val spatialObjectId: String?,
    val plantInstanceId: String?,
    val sourceId: String?,
    val notes: String?,
)
