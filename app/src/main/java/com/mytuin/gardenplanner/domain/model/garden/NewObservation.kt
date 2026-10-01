package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType

/**
 * User-settable fields for a new Observation.
 *
 * The use case assigns id and createdAt, and also generates the
 * activityId for the linked timeline entry (S1 (ii)).
 *
 * observedAt is caller-supplied: the observation time is part of the
 * record's meaning, not its metadata.
 */
data class NewObservation(
    val gardenId: String,
    val observedAt: Long,
    val observationType: ObservationType,
    val confidence: Confidence = Confidence.NOT_ASSESSED,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val plantInstanceId: String? = null,
    val structuredValues: String? = null,
    val notes: String? = null,
)
