package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveSpatialObjectsInGarden
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
    ) {
        operator fun invoke(
            gardenId: String,
            includeArchived: Boolean = false,
        ): Flow<List<SpatialObject>> = repository.observeSpatialObjectsInGarden(gardenId, includeArchived)
    }
