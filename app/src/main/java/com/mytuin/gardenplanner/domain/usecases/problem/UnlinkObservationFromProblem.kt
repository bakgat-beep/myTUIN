package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import javax.inject.Inject

/**
 * Remove a Problem-Observation link.
 *
 * The Observation and the Problem both survive; only the link is
 * removed. A missing link is not an error.
 */
class UnlinkObservationFromProblem
    @Inject
    constructor(
        private val repository: ProblemRepository,
    ) {
        suspend operator fun invoke(
            problemId: String,
            observationId: String,
        ) {
            repository.unlinkObservation(problemId, observationId)
        }
    }
