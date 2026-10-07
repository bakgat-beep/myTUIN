package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Restore an archived Problem.
 *
 * Sets recordStatus = ACTIVE. The Problem's ProblemStatus is
 * untouched.
 */
class RestoreProblem
    @Inject
    constructor(
        private val repository: ProblemRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(problemId: String) {
            repository.restore(id = problemId, restoredAt = clock.nowMillis())
        }
    }
