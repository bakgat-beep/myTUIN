package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationEvidenceRole
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationEvidenceSourceType

/**
 * RecommendationEvidence.
 *
 * V1_DATABASE_SCHEMA §44. Phase 1 step 6.
 *
 * Stable identifier format: "recommendationevidence_<uuid>".
 *
 * One FK to Recommendation, RESTRICT. `source_id` is polymorphic and
 * carries no FK (R3).
 */
@Entity(
    tableName = "recommendation_evidence",
    foreignKeys = [
        ForeignKey(
            entity = RecommendationEntity::class,
            parentColumns = ["id"],
            childColumns = ["recommendation_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["recommendation_id"]),
        Index(value = ["source_type", "source_id"]),
    ],
)
data class RecommendationEvidenceEntity(
    @PrimaryKey
    val id: String,
    val recommendation_id: String,
    val source_type: RecommendationEvidenceSourceType,
    val source_id: String,
    val role: RecommendationEvidenceRole? = null,
    val notes: String? = null,
)
