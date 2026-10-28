package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.model.garden.NewGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.CreateGrowingSpace
import com.mytuin.gardenplanner.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateGrowingSpaceViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val createGrowingSpace: CreateGrowingSpace,
    ) : ViewModel() {
        private val gardenId: String =
            checkNotNull(savedStateHandle[Routes.CREATE_GROWING_SPACE_ARG_GARDEN_ID]) {
                "CreateGrowingSpaceViewModel requires gardenId nav argument"
            }

        private val _uiState = MutableStateFlow(CreateGrowingSpaceUiState())
        val uiState: StateFlow<CreateGrowingSpaceUiState> = _uiState.asStateFlow()

        fun save(formData: GrowingSpaceFormData) {
            if (_uiState.value.isSaving) return
            _uiState.update { it.copy(isSaving = true, saveError = false) }
            viewModelScope.launch {
                try {
                    val geometry = (formData.geometryEdit as? GeometryEdit.Replace)?.geometry
                    val id =
                        createGrowingSpace(
                            NewGrowingSpace(
                                gardenId = gardenId,
                                name = formData.fields.name,
                                spaceType = formData.fields.spaceType,
                                geometry = geometry,
                                lengthMetres = formData.fields.lengthMetres,
                                widthMetres = formData.fields.widthMetres,
                                heightMetres = formData.fields.heightMetres,
                                diameterMetres = formData.fields.diameterMetres,
                                areaSquareMetres = formData.fields.areaSquareMetres,
                                volumeCubicMetres = formData.fields.volumeCubicMetres,
                                description = formData.fields.description,
                                notes = formData.fields.notes,
                            ),
                        )
                    _uiState.update { it.copy(isSaving = false, savedSpaceId = id) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to create growing space", e)
                    _uiState.update { it.copy(isSaving = false, saveError = true) }
                }
            }
        }

        fun onSavedHandled() {
            _uiState.update { it.copy(savedSpaceId = null) }
        }

        private companion object {
            const val TAG = "CreateGrowingSpaceVM"
        }
    }
