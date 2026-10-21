package com.mytuin.gardenplanner.domain.usecases.experiment

import com.mytuin.gardenplanner.domain.repository.ExperimentRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import javax.inject.Inject

class UpdateExperimentStatus
    @Inject
    constructor(
        private val repository: ExperimentRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            experimentId: String,
            status: ExperimentStatus,
        ) {
            repository.updateStatus(
                id = experimentId,
                status = status,
                updatedAt = clock.nowMillis(),
            )
        }
    }
