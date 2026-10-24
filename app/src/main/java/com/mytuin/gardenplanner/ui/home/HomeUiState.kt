package com.mytuin.gardenplanner.ui.home

import com.mytuin.gardenplanner.domain.model.garden.Garden

/**
 * State for the Home screen.
 *
 * D9a-7 scope: Home responds to the existence of a garden, nothing
 * more. Multiple-garden UX (list, switcher) and "garden exists but no
 * content yet" are later steps.
 */
sealed interface HomeUiState {
    /** Initial state before the repository has emitted. */
    data object Loading : HomeUiState

    /** No garden exists. Show the "create your first garden" empty state. */
    data object NoGarden : HomeUiState

    /**
     * At least one garden exists. Show a summary of the first.
     * Multiple-garden selection is deferred to a later phase.
     */
    data class Ready(
        val garden: Garden,
    ) : HomeUiState

    /** Loading the garden list failed. */
    data object Error : HomeUiState
}
