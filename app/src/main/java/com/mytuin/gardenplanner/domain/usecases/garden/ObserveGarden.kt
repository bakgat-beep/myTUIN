package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGarden
    @Inject
    constructor(
        private val repository: GardenRepository,
    ) {
        operator fun invoke(gardenId: String): Flow<Garden?> = repository.observeGarden(gardenId)
    }
