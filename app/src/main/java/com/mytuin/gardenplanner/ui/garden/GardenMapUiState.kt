package com.mytuin.gardenplanner.ui.garden

/**
 * State for the Garden map screen (Step 9f).
 *
 * Five distinct states: loading, no garden, garden-with-no-geometry,
 * ready with shapes, error. The user must understand which of these
 * applies — the empty-state message differs materially between
 * NoGarden ("create a garden") and NoGeometries ("add a shape").
 */
sealed interface GardenMapUiState {
    data object Loading : GardenMapUiState

    data object NoGarden : GardenMapUiState

    data object NoGeometries : GardenMapUiState

    data class Ready(
        val gardenName: String,
        val origin: Wgs84,
        val shapes: List<MapShape>,
        val viewport: MapViewport,
    ) : GardenMapUiState

    data object Error : GardenMapUiState
}
