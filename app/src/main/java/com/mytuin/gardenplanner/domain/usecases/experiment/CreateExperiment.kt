package com.mytuin.gardenplanner.domain.usecases.experiment

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Experiment
import com.mytuin.gardenplanner.domain.model.garden.NewExperiment
import com.mytuin.gardenplanner.domain.repository.ExperimentRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

class CreateExperiment
    @Inject
    constructor(
        private val repository: ExperimentRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewExperiment): String {
            val now = clock.nowMillis()
            val experiment =
                Experiment(
                    id = idGenerator.newExperimentId(),
                    gardenId = input.gardenId,
                    name = input.name,
                    question = input.question,
                    status = input.status,
                    createdAt = now,
                    updatedAt = now,
                    hypothesis = input.hypothesis,
                    startDate = input.startDate,
                    endDate = input.endDate,
                    notes = input.notes,
                )
            repository.insert(experiment)
            return experiment.id
        }
    }
