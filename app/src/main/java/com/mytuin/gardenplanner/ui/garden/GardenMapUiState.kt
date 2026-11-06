package com.mytuin.gardenplanner.ui.garden

/**
 * State for the Garden map screen (Steps 9f and 9h).
 *
 * Six distinct states. The user must understand which applies —
 * the empty-state message differs materially between:
 *   NoGarden        ("create a garden")
 *   NoGeometries    ("add a shape")
 *   AllLayersHidden ("your filters hide everything; reset them")
 *
 * AllLayersHidden is distinct from NoGeometries because the fix is
 * different: reset the filters vs create a shape. Folding them
 * together would make both messages half-wrong in the other case.
 */
sealed interface GardenMapUiState {
    data object Loading : GardenMapUiState

    data object NoGarden : GardenMapUiState

    data object NoGeometries : GardenMapUiState

    data object AllLayersHidden : GardenMapUiState

    data class Ready(
        val gardenName: String,
        val origin: Wgs84,
        val shapes: List<MapShape>,
        val viewport: MapViewport,
    ) : GardenMapUiState

    data object Error : GardenMapUiState
}
