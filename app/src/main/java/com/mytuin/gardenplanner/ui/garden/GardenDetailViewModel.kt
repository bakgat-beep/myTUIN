package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.model.location.Country
import com.mytuin.gardenplanner.domain.repository.CountryRepository
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGarden
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGardenLocation
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
class GardenDetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        observeGarden: ObserveGarden,
        private val updateGardenLocation: UpdateGardenLocation,
        countryRepository: CountryRepository,
    ) : ViewModel() {
        private val gardenId: String =
            checkNotNull(savedStateHandle[Routes.GARDEN_DETAIL_ARG_GARDEN_ID]) {
                "GardenDetailViewModel requires gardenId nav argument"
            }

        private val _uiState = MutableStateFlow(GardenDetailUiState())
        val uiState: StateFlow<GardenDetailUiState> = _uiState.asStateFlow()

        private var priorLocation: NewGardenLocation? = null

        init {
            viewModelScope.launch {
                observeGarden(gardenId)
                    .catch { e ->
                        Log.w(TAG, "Failed to observe garden $gardenId", e)
                        _uiState.update { it.copy(isLoading = false, loadError = true) }
                    }.collect { garden ->
                        _uiState.update {
                            it.copy(
                                garden = garden,
                                isLoading = false,
                                loadError = garden == null,
                            )
                        }
                    }
            }

            viewModelScope.launch {
                try {
                    val countries: List<Country> = countryRepository.getCountries()
                    _uiState.update { it.copy(countries = countries) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to load countries", e)
                } catch (e: java.io.IOException) {
                    Log.w(TAG, "Failed to load countries", e)
                }
            }
        }

        fun openEditSheet() {
            val garden = _uiState.value.garden ?: return
            priorLocation = garden.toNewGardenLocation()
            _uiState.update {
                it.copy(
                    isEditSheetOpen = true,
                    isEditSaving = false,
                    editSaveError = false,
                )
            }
        }

        fun closeEditSheet() {
            _uiState.update {
                it.copy(
                    isEditSheetOpen = false,
                    isEditSaving = false,
                    editSaveError = false,
                )
            }
        }

        fun saveLocation(newLocation: NewGardenLocation) {
            if (_uiState.value.isEditSaving) return
            val prior = priorLocation ?: return
            _uiState.update { it.copy(isEditSaving = true, editSaveError = false) }
            viewModelScope.launch {
                try {
                    updateGardenLocation(gardenId, newLocation)
                    _uiState.update {
                        it.copy(
                            isEditSaving = false,
                            isEditSheetOpen = false,
                            savedEvent = GardenDetailUiState.SavedEvent(prior),
                        )
                    }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to update location", e)
                    _uiState.update {
                        it.copy(isEditSaving = false, editSaveError = true)
                    }
                }
            }
        }

        fun onSavedEventHandled() {
            _uiState.update { it.copy(savedEvent = null) }
        }

        fun undoLocation() {
            val event = _uiState.value.savedEvent ?: return
            viewModelScope.launch {
                try {
                    updateGardenLocation(gardenId, event.priorLocation)
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to undo location update", e)
                }
            }
        }

        private fun Garden.toNewGardenLocation(): NewGardenLocation =
            NewGardenLocation(
                countryCode = countryCode,
                region = region,
                locality = locality,
                latitude = latitude,
                longitude = longitude,
                timezone = timezone,
                hemisphere = hemisphere,
            )

        private companion object {
            const val TAG = "GardenDetailViewModel"
        }
    }
