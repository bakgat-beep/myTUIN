package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import javax.inject.Inject

/**
 * Link an Observation to a Problem as evidence.
 *
 * PROBLEM_VOCABULARIES §42, §22. `evidenceDirection` describes how
 * the Observation relates to the Problem (supports, contradicts,
 * etc.). Null means the direction has not been assessed.
 *
 * Both ids must reference existing rows. The repository validates.
 */
class LinkObservationToProblem
    @Inject
    constructor(
        private val repository: ProblemRepository,
    ) {
        suspend operator fun invoke(
            problemId: String,
            observationId: String,
            evidenceDirection: ProblemEvidenceDirection? = null,
            notes: String? = null,
        ) {
            repository.linkObservation(
                problemId = problemId,
                observationId = observationId,
                evidenceDirection = evidenceDirection,
                notes = notes,
            )
        }
    }
