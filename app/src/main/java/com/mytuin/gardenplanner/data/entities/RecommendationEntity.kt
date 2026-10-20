package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationSubjectType

/**
 * Recommendation.
 *
 * V1_DATABASE_SCHEMA §42. Phase 1 step 6.
 *
 * Stable identifier format: "recommendation_<uuid>".
 *
 * One FK to Garden, RESTRICT. `subject_id` is polymorphic and
 * carries no FK (R2).
 *
 * Status retirement is `RecommendationStatus.EXPIRED` (R7). No
 * RecordStatus column.
 */
@Entity(
    tableName = "recommendation",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["subject_type", "subject_id"]),
        Index(value = ["created_at"]),
        Index(value = ["status"]),
    ],
)
data class RecommendationEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val created_at: Long,
    val subject_type: RecommendationSubjectType,
    val subject_id: String,
    val recommendation_type: ActivityType,
    val status: RecommendationStatus,
    val confidence: Confidence,
    val overall_result: String? = null,
    val expires_at: Long? = null,
    val context: String? = null,
    val notes: String? = null,
)
