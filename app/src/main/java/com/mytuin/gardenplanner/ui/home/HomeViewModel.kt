package com.mytuin.gardenplanner.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGardens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        observeGardens: ObserveGardens,
    ) : ViewModel() {
        val uiState: StateFlow<HomeUiState> =
            observeGardens()
                .map<List<Garden>, HomeUiState> { gardens ->
                    if (gardens.isEmpty()) {
                        HomeUiState.NoGarden
                    } else {
                        HomeUiState.Ready(gardens.first())
                    }
                }.catch { emit(HomeUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = HomeUiState.Loading,
                )
    }
