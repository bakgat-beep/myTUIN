package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a Problem.
 *
 * Sets recordStatus = ARCHIVED. The Problem's own lifecycle status
 * (ProblemStatus) is untouched (P2 (a)). Archiving removes the
 * Problem from default queries without changing its meaning.
 */
class ArchiveProblem
    @Inject
    constructor(
        private val repository: ProblemRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(problemId: String) {
            repository.archive(id = problemId, archivedAt = clock.nowMillis())
        }
    }
