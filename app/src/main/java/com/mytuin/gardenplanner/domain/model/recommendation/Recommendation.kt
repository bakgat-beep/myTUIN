package com.mytuin.gardenplanner.domain.model.recommendation

import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationSubjectType

/**
 * Recommendation — domain model.
 *
 * V1_DATABASE_SCHEMA §42. DATA_MODEL §43.
 *
 * A historical assessment, not an authoritative garden fact. System-
 * generated; not user-initiated (R9).
 *
 * `subjectType` + `subjectId` identify what the recommendation is
 * about, polymorphically. `subjectId` has no FK (R2).
 *
 * `recommendationType` reuses ActivityType (R1), aligning the three
 * sides of the model: Activity is what happened, Plan is what is
 * intended, Recommendation is what is suggested.
 *
 * Status retirement is `RecommendationStatus.EXPIRED` (R7). No
 * separate RecordStatus.
 *
 * Factors and evidence are stored in their own tables and linked by
 * `recommendationId`; a Recommendation alone does not include them
 * inline.
 */
data class Recommendation(
    val id: String,
    val gardenId: String,
    val createdAt: Long,
    val subjectType: RecommendationSubjectType,
    val subjectId: String,
    val recommendationType: ActivityType,
    val status: RecommendationStatus,
    val confidence: Confidence,
    val overallResult: String?,
    val expiresAt: Long?,
    val context: String?,
    val notes: String?,
)
