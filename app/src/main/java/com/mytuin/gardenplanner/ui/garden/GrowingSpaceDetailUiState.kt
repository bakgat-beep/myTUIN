package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace

data class GrowingSpaceDetailUiState(
    val space: GrowingSpace? = null,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
)
