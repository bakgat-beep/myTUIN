package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.model.location.Country

/**
 * State for Garden Detail.
 *
 * Edit flow: sheet open/close, save in-flight, save error, and a
 * savedEvent that carries the prior location for undo (E2, E3 (a)).
 */
data class GardenDetailUiState(
    val garden: Garden? = null,
    val isLoading: Boolean = true,
    val loadError: Boolean = false,
    val countries: List<Country> = emptyList(),
    val isEditSheetOpen: Boolean = false,
    val isEditSaving: Boolean = false,
    val editSaveError: Boolean = false,
    val savedEvent: SavedEvent? = null,
) {
    /** Non-null after a successful save; drives the snackbar and undo. */
    data class SavedEvent(
        val priorLocation: NewGardenLocation,
    )
}
