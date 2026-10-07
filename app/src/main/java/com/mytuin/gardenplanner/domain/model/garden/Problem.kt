package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Problem — domain model.
 *
 * V1_DATABASE_SCHEMA §36. DATA_MODEL §38–§39.
 *
 * A problem or condition in the garden, or a problem the application
 * has identified as a possible issue. Distinct from an Observation
 * (Invariant 5): an Observation records what was seen; a Problem
 * represents a classification or hypothesis. The existence of a
 * Problem does not mean a diagnosis is confirmed (§31, §36).
 *
 * `name` is free text (P5): the specific instance, e.g. "Aphids on
 * the tomato bed". `problemType` is the vocabulary-backed
 * classification.
 *
 * `status` (ProblemStatus) is the problem's own lifecycle. `recordStatus`
 * (RecordStatus) is the record's archival state (P2 (a)). The two are
 * orthogonal: a Problem may be `active` while its record is
 * `archived`.
 *
 * `updatedAt` is present because a Problem is a mutable state record:
 * its status changes over time. No history table (P4); status-change
 * history is a follow-up if a real requirement surfaces.
 *
 * No `priority` (P7), no reasoning state, no outcome (P8).
 */
data class Problem(
    val id: String,
    val gardenId: String,
    val name: String,
    val problemType: ProblemCategory,
    val status: ProblemStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val severity: ProblemSeverity?,
    val confidence: Confidence?,
    val recordStatus: RecordStatus?,
    val description: String?,
    val areaId: String?,
    val growingSpaceId: String?,
    val plantInstanceId: String?,
    val notes: String?,
)
