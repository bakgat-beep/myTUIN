package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemPriority
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Problem.
 *
 * V1_DATABASE_SCHEMA §36. Phase 1 step 8 resolves the P7 omission:
 * `priority` uses ProblemPriority (PROBLEM_VOCABULARIES §17).
 */
@Entity(
    tableName = "problem",
    foreignKeys = [
        ForeignKey(
            entity = GardenEntity::class,
            parentColumns = ["id"],
            childColumns = ["garden_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = AreaEntity::class,
            parentColumns = ["id"],
            childColumns = ["area_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = GrowingSpaceEntity::class,
            parentColumns = ["id"],
            childColumns = ["growing_space_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = PlantInstanceEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_instance_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["garden_id"]),
        Index(value = ["problem_type"]),
        Index(value = ["status"]),
        Index(value = ["area_id"]),
        Index(value = ["growing_space_id"]),
        Index(value = ["plant_instance_id"]),
    ],
)
data class ProblemEntity(
    @PrimaryKey
    val id: String,
    val garden_id: String,
    val name: String,
    val problem_type: ProblemCategory,
    val status: ProblemStatus,
    val created_at: Long,
    val updated_at: Long,
    val severity: ProblemSeverity? = null,
    val confidence: Confidence? = null,
    val record_status: RecordStatus? = null,
    val description: String? = null,
    val area_id: String? = null,
    val growing_space_id: String? = null,
    val plant_instance_id: String? = null,
    val notes: String? = null,
    val priority: ProblemPriority? = null,
)
