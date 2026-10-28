package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.model.garden.NewSpatialObject
import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.usecases.garden.CreateSpatialObject
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
class CreateSpatialObjectViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val createSpatialObject: CreateSpatialObject,
        areaRepository: AreaRepository,
    ) : ViewModel() {
        private val gardenId: String =
            checkNotNull(savedStateHandle[Routes.CREATE_SPATIAL_OBJECT_ARG_GARDEN_ID]) {
                "CreateSpatialObjectViewModel requires gardenId nav argument"
            }

        private val _uiState = MutableStateFlow(CreateSpatialObjectUiState())
        val uiState: StateFlow<CreateSpatialObjectUiState> = _uiState.asStateFlow()

        private val _areas = MutableStateFlow<List<Area>>(emptyList())
        val areas: StateFlow<List<Area>> = _areas.asStateFlow()

        init {
            viewModelScope.launch {
                areaRepository
                    .observeAreasInGarden(gardenId)
                    .catch { e -> Log.w(TAG, "Failed to observe areas for garden $gardenId", e) }
                    .collect { list -> _areas.value = list }
            }
        }

        fun save(formData: SpatialObjectFormData) {
            if (_uiState.value.isSaving) return
            val create = formData as? SpatialObjectFormData.ForCreate ?: return
            _uiState.update { it.copy(isSaving = true, saveError = false) }
            viewModelScope.launch {
                try {
                    val id =
                        createSpatialObject(
                            NewSpatialObject(
                                gardenId = gardenId,
                                objectType = create.objectType,
                                geometry = create.geometry,
                                name = create.name,
                                description = create.description,
                                areaId = create.areaId,
                                notes = create.notes,
                            ),
                        )
                    _uiState.update { it.copy(isSaving = false, savedObjectId = id) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to create spatial object", e)
                    _uiState.update { it.copy(isSaving = false, saveError = true) }
                }
            }
        }

        fun onSavedHandled() {
            _uiState.update { it.copy(savedObjectId = null) }
        }

        private companion object {
            const val TAG = "CreateSpatialObjectVM"
        }
    }
