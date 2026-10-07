package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection

/**
 * A link between a Problem and an Observation that provides evidence
 * for it.
 *
 * V1_DATABASE_SCHEMA §55. PROBLEM_VOCABULARIES §22, §42.
 *
 * A Problem may have zero or more linked Observations (a suspected
 * problem may be based on nothing yet). An Observation may support
 * zero or more Problems (§42 step 4: "one or more possible causes").
 *
 * `evidenceDirection` is per-link. The same Observation may support
 * one Problem and contradict another.
 *
 * Not a standalone entity. Identity is the (problemId, observationId)
 * pair.
 */
data class ProblemObservation(
    val problemId: String,
    val observationId: String,
    val evidenceDirection: ProblemEvidenceDirection?,
    val notes: String?,
)
