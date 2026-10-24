package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.NewGarden
import com.mytuin.gardenplanner.domain.usecases.garden.CreateGarden
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CreateGardenViewModel
    @Inject
    constructor(
        private val createGarden: CreateGarden,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CreateGardenUiState())
        val uiState: StateFlow<CreateGardenUiState> = _uiState.asStateFlow()

        fun onNameChange(value: String) {
            _uiState.update { it.copy(name = value, nameError = null) }
        }

        fun onDescriptionChange(value: String) {
            _uiState.update { it.copy(description = value) }
        }

        fun onSave() {
            val current = _uiState.value
            if (current.isSaving) return
            if (current.name.isBlank()) {
                _uiState.update {
                    it.copy(nameError = CreateGardenUiState.NameError.BLANK)
                }
                return
            }
            _uiState.update {
                it.copy(isSaving = true, nameError = null, saveError = null)
            }
            viewModelScope.launch {
                try {
                    val id =
                        createGarden(
                            NewGarden(
                                name = current.name,
                                description = current.description.ifBlank { null },
                            ),
                        )
                    _uiState.update {
                        it.copy(isSaving = false, savedGardenId = id)
                    }
                } catch (e: ValidationError) {
                    Log.w(TAG, "CreateGarden validation failed: field=${e.field}", e)
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            saveError = CreateGardenUiState.SaveError.GENERIC,
                        )
                    }
                } catch (e: DomainError) {
                    Log.w(TAG, "CreateGarden failed: ${e.message}", e)
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            saveError = CreateGardenUiState.SaveError.GENERIC,
                        )
                    }
                }
            }
        }

        fun onSaveHandled() {
            _uiState.update { it.copy(savedGardenId = null, saveError = null) }
        }

        private companion object {
            const val TAG = "CreateGardenViewModel"
        }
    }
