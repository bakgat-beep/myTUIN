package com.mytuin.gardenplanner.ui.garden

/**
 * Durable edit state for the Garden map (Step 9g).
 *
 * Selection, save-in-flight and save-error are ViewModel state
 * because they survive recomposition and configuration change.
 * Mid-gesture state lives in MapEditorController.
 */
data class GardenMapEditUiState(
    val selectedShapeId: String? = null,
    val isSaving: Boolean = false,
    val error: Boolean = false,
)
