package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementUnit

/**
 * User-settable fields for a new Measurement.
 *
 * The use case assigns id and createdAt.
 */
data class NewMeasurement(
    val gardenId: String,
    val property: MeasurementProperty,
    val value: Double,
    val unit: MeasurementUnit,
    val measuredAt: KnownDate,
    val confidence: Confidence = Confidence.NOT_ASSESSED,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val plantInstanceId: String? = null,
    val notes: String? = null,
)
