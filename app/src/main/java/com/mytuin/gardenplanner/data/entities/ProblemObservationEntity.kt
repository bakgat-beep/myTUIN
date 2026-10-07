package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection

/**
 * ProblemObservation.
 *
 * V1_DATABASE_SCHEMA §55. PROBLEM_VOCABULARIES §22, §42.
 *
 * Composite primary key (problem_id, observation_id). The leading
 * column (problem_id) satisfies the FK index requirement; a separate
 * index on observation_id is declared for the reverse query.
 *
 * Both foreign keys RESTRICT: neither a Problem nor an Observation
 * may be deleted while a link references it.
 *
 * `evidence_direction` is per-link and nullable. Null means the
 * direction has not been assessed.
 */
@Entity(
    tableName = "problem_observation",
    primaryKeys = ["problem_id", "observation_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProblemEntity::class,
            parentColumns = ["id"],
            childColumns = ["problem_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = ObservationEntity::class,
            parentColumns = ["id"],
            childColumns = ["observation_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index(value = ["observation_id"]),
    ],
)
data class ProblemObservationEntity(
    val problem_id: String,
    val observation_id: String,
    val evidence_direction: ProblemEvidenceDirection? = null,
    val notes: String? = null,
)
