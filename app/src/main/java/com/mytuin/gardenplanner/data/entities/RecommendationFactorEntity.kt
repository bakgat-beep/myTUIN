package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationFactorResult
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationReasonType
import com.mytuin.gardenplanner.domain.vocabulary.SuitabilityDimension

/**
 * RecommendationFactor.
 *
 * V1_DATABASE_SCHEMA §43. Phase 1 step 6.
 *
 * Stable identifier format: "recommendationfactor_<uuid>".
 *
 * One FK to Recommendation, RESTRICT.
 */
@Entity(
    tableName = "recommendation_factor",
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
        Index(value = ["dimension"]),
    ],
)
data class RecommendationFactorEntity(
    @PrimaryKey
    val id: String,
    val recommendation_id: String,
    val dimension: SuitabilityDimension,
    val result: RecommendationFactorResult,
    val confidence: Confidence,
    val weight: Double? = null,
    val reason_type: RecommendationReasonType? = null,
    val notes: String? = null,
)
