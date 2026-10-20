package com.mytuin.gardenplanner.domain.model.recommendation

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationFactorResult
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationReasonType
import com.mytuin.gardenplanner.domain.vocabulary.SuitabilityDimension

/**
 * RecommendationFactor — domain model.
 *
 * V1_DATABASE_SCHEMA §43. DATA_MODEL §44.
 *
 * One dimension contributing to a Recommendation. A factor result
 * (`result`) answers "how suitable is this dimension"; a reason
 * (`reasonType`) answers "why did this affect the recommendation".
 * Both may be present (PLANNING_VOCABULARIES §9).
 *
 * `reasonType` and `notes` live here (R4), not on a separate reasons
 * entity. A single recommendation may carry multiple factors, each
 * with its own reason.
 */
data class RecommendationFactor(
    val id: String,
    val recommendationId: String,
    val dimension: SuitabilityDimension,
    val result: RecommendationFactorResult,
    val confidence: Confidence,
    val weight: Double?,
    val reasonType: RecommendationReasonType?,
    val notes: String?,
)
