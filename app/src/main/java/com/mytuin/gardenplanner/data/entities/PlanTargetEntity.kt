package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType

/**
 * PlanTarget.
 *
 * V1_DATABASE_SCHEMA §41. DATA_MODEL §41.
 *
 * Composite primary key (plan_id, target_type, target_id). The
 * leading column (plan_id) satisfies the FK index requirement; the
 * composite key also serves as the natural identity of the target
 * within a plan.
 *
 * `target_id` is untyped (String) and carries no foreign key
 * (PL2 (a)). Existence of the target is pre-checked by
 * PlanRepositoryImpl.addTarget based on target_type. The tradeoff
 * is recorded in the DEC for Step 3f.
 *
 * `completed_at` is per-target (PL10). Null means not completed.
 * §29: partial completion across targets is directly supported.
 */
@Entity(
    tableName = "plan_target",
    primaryKeys = ["plan_id", "target_type", "target_id"],
    foreignKeys = [
        ForeignKey(
            entity = PlanEntity::class,
            parentColumns = ["id"],
            childColumns = ["plan_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["target_type", "target_id"]),
    ],
)
data class PlanTargetEntity(
    val plan_id: String,
    val target_type: PlanTargetType,
    val target_id: String,
    val completed_at: Long? = null,
    val notes: String? = null,
)
