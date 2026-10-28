package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace

data class EditGrowingSpaceUiState(
    val space: GrowingSpace? = null,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val isSaving: Boolean = false,
    val saveError: Boolean = false,
    val savedEvent: Boolean = false,
)
