package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObjectsInGarden
import com.mytuin.gardenplanner.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class SpatialObjectsListViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeSpatialObjectsInGarden: ObserveSpatialObjectsInGarden,
    ) : ViewModel() {
        private val gardenId: String =
            checkNotNull(savedStateHandle[Routes.SPATIAL_OBJECTS_ARG_GARDEN_ID]) {
                "SpatialObjectsListViewModel requires gardenId nav argument"
            }

        private val showArchived = MutableStateFlow(false)

        // Explicit Flow<SpatialObjectsListUiState> widens combine's
        // inferred subtype so .catch can emit the Error arm.
        private val baseFlow: Flow<SpatialObjectsListUiState> =
            combine(
                observeSpatialObjectsInGarden(gardenId, includeArchived = false),
                observeSpatialObjectsInGarden(gardenId, includeArchived = true),
                showArchived,
            ) { active, all, showArch ->
                val objects: List<SpatialObject> = if (showArch) all else active
                SpatialObjectsListUiState.Ready(objects = objects, showArchived = showArch)
            }

        val uiState: StateFlow<SpatialObjectsListUiState> =
            baseFlow
                .catch { emit(SpatialObjectsListUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = SpatialObjectsListUiState.Loading,
                )

        fun toggleShowArchived() {
            showArchived.update { !it }
        }
    }
