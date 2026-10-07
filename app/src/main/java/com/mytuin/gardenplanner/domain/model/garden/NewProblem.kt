package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus

/**
 * User-settable fields for a new Problem.
 *
 * The use case assigns id, createdAt, updatedAt and recordStatus.
 * `status` defaults to SUSPECTED (P6) and may be overridden.
 */
data class NewProblem(
    val gardenId: String,
    val name: String,
    val problemType: ProblemCategory,
    val status: ProblemStatus = ProblemStatus.SUSPECTED,
    val severity: ProblemSeverity? = null,
    val confidence: Confidence? = null,
    val description: String? = null,
    val areaId: String? = null,
    val growingSpaceId: String? = null,
    val plantInstanceId: String? = null,
    val notes: String? = null,
)
