package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import javax.inject.Inject

/**
 * Change a Problem's lifecycle status.
 *
 * PROBLEM_VOCABULARIES §38: problems retain their historical
 * lifecycle. This use case updates the current status and
 * `updatedAt`. No history table in 3e (P4); if status-change history
 * is required later, it is a follow-up with a DEC.
 */
class UpdateProblemStatus
    @Inject
    constructor(
        private val repository: ProblemRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            problemId: String,
            status: ProblemStatus,
        ) {
            repository.updateStatus(
                id = problemId,
                status = status,
                updatedAt = clock.nowMillis(),
            )
        }
    }
