package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.RestoreSpatialObject
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
class SpatialObjectDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeSpatialObject: ObserveSpatialObject,
        private val archiveSpatialObject: ArchiveSpatialObject,
        private val restoreSpatialObject: RestoreSpatialObject,
    ) : ViewModel() {
        private val spatialObjectId: String =
            checkNotNull(savedStateHandle[Routes.SPATIAL_OBJECT_DETAIL_ARG_ID]) {
                "SpatialObjectDetailViewModel requires spatialObjectId nav argument"
            }

        private val _uiState = MutableStateFlow(SpatialObjectDetailUiState())
        val uiState: StateFlow<SpatialObjectDetailUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                observeSpatialObject(spatialObjectId)
                    .catch { e ->
                        Log.w(TAG, "Failed to observe spatial object $spatialObjectId", e)
                        _uiState.update { it.copy(isLoading = false, loadError = true) }
                    }.collect { spatialObject ->
                        _uiState.update {
                            it.copy(
                                spatialObject = spatialObject,
                                isLoading = false,
                                loadError = spatialObject == null,
                            )
                        }
                    }
            }
        }

        fun archive() {
            viewModelScope.launch {
                try {
                    archiveSpatialObject(spatialObjectId)
                    _uiState.update {
                        it.copy(finishedEvent = SpatialObjectDetailUiState.FinishedEvent.ARCHIVED)
                    }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to archive spatial object", e)
                }
            }
        }

        fun restore() {
            viewModelScope.launch {
                try {
                    restoreSpatialObject(spatialObjectId)
                    _uiState.update {
                        it.copy(finishedEvent = SpatialObjectDetailUiState.FinishedEvent.RESTORED)
                    }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to restore spatial object", e)
                }
            }
        }

        fun onFinishedHandled() {
            _uiState.update { it.copy(finishedEvent = null) }
        }

        private companion object {
            const val TAG = "SpatialObjectDetailVM"
        }
    }
