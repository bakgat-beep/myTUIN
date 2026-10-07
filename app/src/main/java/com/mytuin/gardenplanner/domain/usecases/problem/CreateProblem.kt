package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewProblem
import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Record a new Problem.
 *
 * Assigns id, recordStatus (ACTIVE), createdAt and updatedAt.
 * `status` is taken from the input, defaulting to SUSPECTED (P6).
 *
 * Returns the new Problem's id.
 */
class CreateProblem
    @Inject
    constructor(
        private val repository: ProblemRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewProblem): String {
            val now = clock.nowMillis()
            val problem =
                Problem(
                    id = idGenerator.newProblemId(),
                    gardenId = input.gardenId,
                    name = input.name,
                    problemType = input.problemType,
                    status = input.status,
                    createdAt = now,
                    updatedAt = now,
                    severity = input.severity,
                    confidence = input.confidence,
                    recordStatus = RecordStatus.ACTIVE,
                    description = input.description,
                    areaId = input.areaId,
                    growingSpaceId = input.growingSpaceId,
                    plantInstanceId = input.plantInstanceId,
                    notes = input.notes,
                )
            repository.insert(problem)
            return problem.id
        }
    }
