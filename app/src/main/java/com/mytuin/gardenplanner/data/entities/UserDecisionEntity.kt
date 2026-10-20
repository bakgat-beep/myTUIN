package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.UserDecisionType

/**
 * UserDecision.
 *
 * V1_DATABASE_SCHEMA §46. Phase 1 step 6.
 *
 * Stable identifier format: "userdecision_<uuid>".
 *
 * Three foreign keys, all RESTRICT. recommendation_id and plan_id
 * are nullable; at least one must be non-null (R6). SQLite cannot
 * express that through Room's @Entity; the mapper enforces it on
 * read.
 */
@Entity(
    tableName = "user_decision",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = RecommendationEntity::class,
            parentColumns = ["id"],
            childColumns = ["recommendation_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["recommendation_id"]),
        Index(value = ["plan_id"]),
        Index(value = ["decided_at"]),
    ],
)
data class UserDecisionEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val decision_type: UserDecisionType,
    val decided_at: Long,
    val created_at: Long,
    val recommendation_id: String? = null,
    val plan_id: String? = null,
    val reason: String? = null,
    val notes: String? = null,
)
