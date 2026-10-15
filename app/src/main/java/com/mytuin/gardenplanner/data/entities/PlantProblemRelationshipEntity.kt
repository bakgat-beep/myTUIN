package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.GrowthStage
import com.mytuin.gardenplanner.domain.vocabulary.PlantProblemRelationshipType
import com.mytuin.gardenplanner.domain.vocabulary.ProblemResponse
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity

/**
 * PlantProblemRelationship.
 *
 * V1_DATABASE_SCHEMA §23. Phase 1 step 4d.
 *
 * Composite primary key (plant_id, problem_id, relationship_type).
 * `cultivar_id` is non-key (P41D-9): one row per (plant, problem,
 * relationship type).
 */
@Entity(
    tableName = "plant_problem_relationship",
    primaryKeys = ["plant_id", "problem_id", "relationship_type"],
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plant_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = ProblemEntity::class,
            parentColumns = ["id"],
            childColumns = ["problem_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = CultivarEntity::class,
            parentColumns = ["id"],
            childColumns = ["cultivar_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = SourceEntity::class,
            parentColumns = ["id"],
            childColumns = ["source_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["problem_id"]),
        Index(value = ["cultivar_id"]),
        Index(value = ["source_id"]),
    ],
)
data class PlantProblemRelationshipEntity(
    val plant_id: String,
    val problem_id: String,
    val relationship_type: PlantProblemRelationshipType,
    val cultivar_id: String? = null,
    val response_characteristic: ProblemResponse? = null,
    val growth_stage: GrowthStage? = null,
    val severity: ProblemSeverity? = null,
    val reason: String? = null,
    val source_id: String? = null,
    val confidence: Confidence? = null,
    val notes: String? = null,
)
