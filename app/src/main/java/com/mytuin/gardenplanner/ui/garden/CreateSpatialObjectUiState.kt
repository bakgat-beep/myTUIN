package com.mytuin.gardenplanner.ui.garden

data class CreateSpatialObjectUiState(
    val isSaving: Boolean = false,
    val saveError: Boolean = false,
    val savedObjectId: String? = null,
)
