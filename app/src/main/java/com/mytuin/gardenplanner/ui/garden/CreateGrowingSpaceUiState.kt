package com.mytuin.gardenplanner.ui.garden

data class CreateGrowingSpaceUiState(
    val isSaving: Boolean = false,
    val saveError: Boolean = false,
    val savedSpaceId: String? = null,
)
