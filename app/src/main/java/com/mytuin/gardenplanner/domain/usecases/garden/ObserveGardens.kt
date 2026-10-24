package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Observe the list of gardens (excluding archived).
 *
 * Thin wrapper around GardenRepository.observeGardens(). Present as a
 * use case because the specs require UI to reach the domain through
 * use cases rather than through repositories directly
 * (PHASE_0_PROJECT_FOUNDATION §14).
 */
class ObserveGardens
    @Inject
    constructor(
        private val repository: GardenRepository,
    ) {
        operator fun invoke(): Flow<List<Garden>> = repository.observeGardens()
    }
