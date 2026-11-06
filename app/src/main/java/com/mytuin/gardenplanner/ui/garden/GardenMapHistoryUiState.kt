package com.mytuin.gardenplanner.ui.garden

/**
 * State for the Garden history screen (Step 9i).
 *
 * Six states. The three "nothing to show" states are distinct
 * because the fix in each case differs materially:
 *   NoGarden  → create a garden
 *   NoShapes  → add a shape to the map
 *   NoHistory → edit an existing shape to record history
 *
 * Ready carries the whole timeline plus the current scrub position
 * and the shapes reconstructed at that instant. The screen does no
 * reconstruction of its own — it renders shapes as given.
 */
sealed interface GardenMapHistoryUiState {
    data object Loading : GardenMapHistoryUiState

    data object NoGarden : GardenMapHistoryUiState

    data object NoShapes : GardenMapHistoryUiState

    data object NoHistory : GardenMapHistoryUiState

    data object Error : GardenMapHistoryUiState

    data class Ready(
        val gardenName: String,
        val origin: Wgs84,
        val timeline: GardenHistoryTimeline,
        val currentTime: Long,
        val shapes: List<MapShape>,
        val viewport: MapViewport,
    ) : GardenMapHistoryUiState
}
