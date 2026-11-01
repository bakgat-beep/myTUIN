package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAreasInGarden
    @Inject
    constructor(
        private val repository: AreaRepository,
    ) {
        operator fun invoke(gardenId: String): Flow<List<Area>> = repository.observeAreasInGarden(gardenId, includeArchived = false)
    }
