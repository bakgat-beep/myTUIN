package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemPriority
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * Problem — domain model.
 *
 * Step 8 resolves the P7 omission: `priority` uses ProblemPriority
 * from PROBLEM_VOCABULARIES §17.
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
    val priority: ProblemPriority?,
)
