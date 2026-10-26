package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace

sealed interface GrowingSpacesListUiState {
    data object Loading : GrowingSpacesListUiState

    data object NoSpaces : GrowingSpacesListUiState

    data class Ready(
        val spaces: List<GrowingSpace>,
    ) : GrowingSpacesListUiState

    data object Error : GrowingSpacesListUiState
}
