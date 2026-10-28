package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateSpatialObjectFields
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
class EditSpatialObjectViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeSpatialObject: com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObject,
        private val updateSpatialObjectFields: UpdateSpatialObjectFields,
        areaRepository: AreaRepository,
    ) : ViewModel() {
        private val spatialObjectId: String =
            checkNotNull(savedStateHandle[Routes.EDIT_SPATIAL_OBJECT_ARG_ID]) {
                "EditSpatialObjectViewModel requires spatialObjectId nav argument"
            }

        private val _uiState = MutableStateFlow(EditSpatialObjectUiState())
        val uiState: StateFlow<EditSpatialObjectUiState> = _uiState.asStateFlow()

        private val _areas = MutableStateFlow<List<Area>>(emptyList())
        val areas: StateFlow<List<Area>> = _areas.asStateFlow()

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
                        if (spatialObject != null) {
                            observeAreas(spatialObject.gardenId, areaRepository)
                        }
                    }
            }
        }

        private fun observeAreas(
            gardenId: String,
            areaRepository: AreaRepository,
        ) {
            viewModelScope.launch {
                areaRepository
                    .observeAreasInGarden(gardenId)
                    .catch { e -> Log.w(TAG, "Failed to observe areas for garden $gardenId", e) }
                    .collect { list -> _areas.value = list }
            }
        }

        fun save(formData: SpatialObjectFormData) {
            if (_uiState.value.isSaving) return
            val edit = formData as? SpatialObjectFormData.ForEdit ?: return
            _uiState.update { it.copy(isSaving = true, saveError = false) }
            viewModelScope.launch {
                try {
                    updateSpatialObjectFields(spatialObjectId, edit.fields)
                    _uiState.update { it.copy(isSaving = false, savedEvent = true) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to save spatial object", e)
                    _uiState.update { it.copy(isSaving = false, saveError = true) }
                }
            }
        }

        fun onSavedHandled() {
            _uiState.update { it.copy(savedEvent = false) }
        }

        private companion object {
            const val TAG = "EditSpatialObjectVM"
        }
    }
