package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGrowingSpacesInGarden
    @Inject
    constructor(
        private val repository: GrowingSpaceRepository,
    ) {
        operator fun invoke(gardenId: String): Flow<List<GrowingSpace>> = repository.observeGrowingSpacesInGarden(gardenId)
    }
