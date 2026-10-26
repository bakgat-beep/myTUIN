package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveGrowingSpace
    @Inject
    constructor(
        private val repository: GrowingSpaceRepository,
    ) {
        operator fun invoke(growingSpaceId: String): Flow<GrowingSpace?> = repository.observeGrowingSpace(growingSpaceId)
    }
