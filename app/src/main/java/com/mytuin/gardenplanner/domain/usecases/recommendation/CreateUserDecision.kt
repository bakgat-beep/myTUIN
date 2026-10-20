package com.mytuin.gardenplanner.domain.usecases.recommendation

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.recommendation.NewUserDecision
import com.mytuin.gardenplanner.domain.model.recommendation.UserDecision
import com.mytuin.gardenplanner.domain.repository.UserDecisionRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Record a new UserDecision.
 *
 * Assigns id and createdAt. Returns the new id.
 *
 * The repository pre-checks that the garden, the recommendation (if
 * supplied) and the plan (if supplied) exist. The mapper rejects a
 * row with both recommendation_id and plan_id null on read (R6).
 */
class CreateUserDecision
    @Inject
    constructor(
        private val repository: UserDecisionRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewUserDecision): String {
            val decision =
                UserDecision(
                    id = idGenerator.newUserDecisionId(),
                    gardenId = input.gardenId,
                    decisionType = input.decisionType,
                    decidedAt = input.decidedAt,
                    createdAt = clock.nowMillis(),
                    recommendationId = input.recommendationId,
                    planId = input.planId,
                    reason = input.reason,
                    notes = input.notes,
                )
            repository.insert(decision)
            return decision.id
        }
    }
