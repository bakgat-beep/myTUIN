package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.SpatialObject

data class SpatialObjectDetailUiState(
    val spatialObject: SpatialObject? = null,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val finishedEvent: FinishedEvent? = null,
) {
    enum class FinishedEvent {
        ARCHIVED,
        RESTORED,
    }
}
