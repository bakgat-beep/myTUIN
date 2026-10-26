package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpacesInGarden
import com.mytuin.gardenplanner.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class GrowingSpacesListViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeGrowingSpacesInGarden: ObserveGrowingSpacesInGarden,
    ) : ViewModel() {
        private val gardenId: String =
            checkNotNull(savedStateHandle[Routes.GROWING_SPACES_ARG_GARDEN_ID]) {
                "GrowingSpacesListViewModel requires gardenId nav argument"
            }

        val uiState: StateFlow<GrowingSpacesListUiState> =
            observeGrowingSpacesInGarden(gardenId)
                .map<List<GrowingSpace>, GrowingSpacesListUiState> { spaces ->
                    if (spaces.isEmpty()) {
                        GrowingSpacesListUiState.NoSpaces
                    } else {
                        GrowingSpacesListUiState.Ready(spaces)
                    }
                }.catch { emit(GrowingSpacesListUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = GrowingSpacesListUiState.Loading,
                )
    }
