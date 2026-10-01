package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType

/**
 * Observation — domain model.
 *
 * V1_DATABASE_SCHEMA §30. DATA_MODEL §23–§26.
 *
 * An Observation records something noticed in the garden. It is not
 * a diagnosis (§31, Invariant 5). It is not a Measurement (§32–33).
 *
 * S1 (ii): every Observation is linked to an Activity of type
 * `observation`. The Activity is the timeline entry; the Observation
 * is its content. activityId carries that link.
 *
 * O5: no status column. Archival goes through the linked Activity:
 * archiving the Observation archives its Activity.
 *
 * O2 (a): structuredValues is an opaque JSON payload for Step 3b.
 * A typed sealed hierarchy replaces it when a concrete UI or
 * recommendation rule needs specific fields.
 */
data class Observation(
    val id: String,
    val activityId: String,
    val gardenId: String,
    val observedAt: Long,
    val observationType: ObservationType,
    val confidence: Confidence,
    val createdAt: Long,
    val areaId: String?,
    val growingSpaceId: String?,
    val spatialObjectId: String?,
    val plantInstanceId: String?,
    val structuredValues: String?,
    val notes: String?,
)
