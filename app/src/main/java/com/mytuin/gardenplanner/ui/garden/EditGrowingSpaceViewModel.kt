package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceFields
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceGeometry
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
class EditGrowingSpaceViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeGrowingSpace: ObserveGrowingSpace,
        private val updateGrowingSpaceFields: UpdateGrowingSpaceFields,
        private val updateGrowingSpaceGeometry: UpdateGrowingSpaceGeometry,
    ) : ViewModel() {
        private val growingSpaceId: String =
            checkNotNull(savedStateHandle[Routes.EDIT_GROWING_SPACE_ARG_ID]) {
                "EditGrowingSpaceViewModel requires growingSpaceId nav argument"
            }

        private val _uiState = MutableStateFlow(EditGrowingSpaceUiState())
        val uiState: StateFlow<EditGrowingSpaceUiState> = _uiState.asStateFlow()

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

        fun save(formData: GrowingSpaceFormData) {
            if (_uiState.value.isSaving) return
            _uiState.update { it.copy(isSaving = true, saveError = false) }
            viewModelScope.launch {
                try {
                    updateGrowingSpaceFields(growingSpaceId, formData.fields)
                    when (val edit = formData.geometryEdit) {
                        is GeometryEdit.Unchanged -> Unit
                        is GeometryEdit.Replace ->
                            updateGrowingSpaceGeometry(
                                growingSpaceId = growingSpaceId,
                                newGeometry = edit.geometry,
                                reason = "Edited via form",
                            )
                    }
                    _uiState.update { it.copy(isSaving = false, savedEvent = true) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to save growing space", e)
                    _uiState.update { it.copy(isSaving = false, saveError = true) }
                }
            }
        }

        fun onSavedHandled() {
            _uiState.update { it.copy(savedEvent = false) }
        }

        private companion object {
            const val TAG = "EditGrowingSpaceVM"
        }
    }
