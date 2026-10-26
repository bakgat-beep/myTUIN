package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpace
import com.mytuin.gardenplanner.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GrowingSpaceDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeGrowingSpace: ObserveGrowingSpace,
    ) : ViewModel() {
        private val growingSpaceId: String =
            checkNotNull(savedStateHandle[Routes.GROWING_SPACE_DETAIL_ARG_ID]) {
                "GrowingSpaceDetailViewModel requires growingSpaceId nav argument"
            }

        private val _uiState = MutableStateFlow(GrowingSpaceDetailUiState())
        val uiState: StateFlow<GrowingSpaceDetailUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                observeGrowingSpace(growingSpaceId)
                    .catch { e ->
                        Log.w(TAG, "Failed to observe growing space $growingSpaceId", e)
                        _uiState.update { it.copy(isLoading = false, loadError = true) }
                    }.collect { space ->
                        _uiState.update {
                            it.copy(
                                space = space,
                                isLoading = false,
                                loadError = space == null,
                            )
                        }
                    }
            }
        }

        private companion object {
            const val TAG = "GrowingSpaceDetailViewModel"
        }
    }
