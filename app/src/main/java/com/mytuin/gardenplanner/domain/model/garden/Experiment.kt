package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus

/**
 * Experiment — domain model.
 *
 * V1_DATABASE_SCHEMA §49. DATA_MODEL §55.
 *
 * A deliberate comparison or testing structure. Optional advanced
 * capability (§49, DATA_MODEL §83). Reuses ordinary garden records
 * via the experiment_link join table; does not create a parallel
 * activity or observation system (§50).
 *
 * `updatedAt` is not listed in §49 but is added here, matching the
 * state-record pattern (Garden, Area, Problem, Plan, SoilRecord). A
 * status change without a timestamp would leave the change
 * untraceable. Flagged in the Step 7 commit message.
 *
 * No `record_status`: the lifecycle is captured by
 * `ExperimentStatus` (§18).
 */
data class Experiment(
    val id: String,
    val gardenId: String,
    val name: String,
    val question: String,
    val status: ExperimentStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val hypothesis: String?,
    val startDate: KnownDate?,
    val endDate: KnownDate?,
    val notes: String?,
)
