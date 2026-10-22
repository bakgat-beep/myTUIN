package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit

data class NewMeasurement(
    val gardenId: String,
    val property: MeasurementProperty,
    val value: Double,
    val unit: QuantityUnit,
    val measuredAt: KnownDate,
    val confidence: Confidence = Confidence.NOT_ASSESSED,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val plantInstanceId: String? = null,
    val sourceId: String? = null,
    val notes: String? = null,
)
