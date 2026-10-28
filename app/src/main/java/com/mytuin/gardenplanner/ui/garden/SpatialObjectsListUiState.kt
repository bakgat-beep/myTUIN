package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.SpatialObject

sealed interface SpatialObjectsListUiState {
    data object Loading : SpatialObjectsListUiState

    data class Ready(
        val objects: List<SpatialObject>,
        val showArchived: Boolean,
    ) : SpatialObjectsListUiState

    data object Error : SpatialObjectsListUiState
}
