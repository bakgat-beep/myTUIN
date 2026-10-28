package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveSpatialObject
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
    ) {
        operator fun invoke(id: String): Flow<SpatialObject?> = repository.observeSpatialObject(id)
    }
