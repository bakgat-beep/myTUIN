package com.mytuin.gardenplanner.domain.model.recommendation

import com.mytuin.gardenplanner.domain.vocabulary.RecommendationEvidenceRole
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationEvidenceSourceType

/**
 * RecommendationEvidence — domain model.
 *
 * V1_DATABASE_SCHEMA §44. DATA_MODEL §45.
 *
 * Links a Recommendation to the records that materially influenced
 * it. `sourceType` + `sourceId` identify the source polymorphically;
 * `sourceId` has no FK (R3).
 *
 * `role` (R5) is a structured classification of how the evidence
 * relates to the recommendation, mirroring
 * ProblemEvidenceDirection's value set as a distinct vocabulary.
 */
data class RecommendationEvidence(
    val id: String,
    val recommendationId: String,
    val sourceType: RecommendationEvidenceSourceType,
    val sourceId: String,
    val role: RecommendationEvidenceRole?,
    val notes: String?,
)
