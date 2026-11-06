package com.mytuin.gardenplanner.ui.garden

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.error.DomainError
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceHistoryRepository
import com.mytuin.gardenplanner.domain.repository.SpatialObjectHistoryRepository
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceGeometry
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateSpatialObjectGeometry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Edit actions for the Garden map (Step 9g).
 *
 * Separated from GardenMapViewModel (observation) so each stays
 * under the LongMethod threshold and has a single responsibility.
 *
 * Actions take a MapShape argument rather than a bare id: the shape
 * carries its kind (growing space vs spatial object), which decides
 * which use case fires. The composable already has the shape in
 * uiState.shapes, so passing it back avoids a second observation
 * and any potential for stale data between the two ViewModels.
 *
 * Undo is single-step: reads the most recent history row via the
 * history repository and applies it through the same update use
 * case that normal edits use. Per DEC-041 the undo therefore writes
 * a new history row — the append-only invariant holds.
 */
@HiltViewModel
class GardenMapEditViewModel
    @Inject
    constructor(
        private val updateGrowingSpaceGeometry: UpdateGrowingSpaceGeometry,
        private val updateSpatialObjectGeometry: UpdateSpatialObjectGeometry,
        private val archiveGrowingSpace: ArchiveGrowingSpace,
        private val archiveSpatialObject: ArchiveSpatialObject,
        private val growingSpaceHistoryRepository: GrowingSpaceHistoryRepository,
        private val spatialObjectHistoryRepository: SpatialObjectHistoryRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(GardenMapEditUiState())
        val uiState: StateFlow<GardenMapEditUiState> = _uiState.asStateFlow()

        fun selectShape(id: String) {
            _uiState.update { it.copy(selectedShapeId = id) }
        }

        fun clearSelection() {
            _uiState.update { it.copy(selectedShapeId = null) }
        }

        fun saveGeometry(
            shape: MapShape,
            geometry: Geometry,
        ) {
            if (_uiState.value.isSaving) return
            _uiState.update { it.copy(isSaving = true, error = false) }
            viewModelScope.launch {
                try {
                    when (shape.kind) {
                        is MapShapeKind.GrowingSpace ->
                            updateGrowingSpaceGeometry(shape.id, geometry, MAP_EDIT_REASON)
                        is MapShapeKind.SpatialObject ->
                            updateSpatialObjectGeometry(shape.id, geometry, MAP_EDIT_REASON)
                        MapShapeKind.Area -> Unit
                    }
                    _uiState.update { it.copy(isSaving = false) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to save geometry for ${shape.id}", e)
                    _uiState.update { it.copy(isSaving = false, error = true) }
                }
            }
        }

        fun archiveSelected(shape: MapShape) {
            if (_uiState.value.isSaving) return
            _uiState.update { it.copy(isSaving = true, error = false) }
            viewModelScope.launch {
                try {
                    when (shape.kind) {
                        is MapShapeKind.GrowingSpace -> archiveGrowingSpace(shape.id)
                        is MapShapeKind.SpatialObject -> archiveSpatialObject(shape.id)
                        MapShapeKind.Area -> Unit
                    }
                    _uiState.update { it.copy(isSaving = false, selectedShapeId = null) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to archive ${shape.id}", e)
                    _uiState.update { it.copy(isSaving = false, error = true) }
                }
            }
        }

        fun undoLastEdit(shape: MapShape) {
            if (_uiState.value.isSaving) return
            _uiState.update { it.copy(isSaving = true, error = false) }
            viewModelScope.launch {
                try {
                    when (shape.kind) {
                        is MapShapeKind.GrowingSpace -> {
                            val latest = growingSpaceHistoryRepository.getHistoryForSpace(shape.id).firstOrNull()
                            if (latest != null) {
                                updateGrowingSpaceGeometry(shape.id, latest.geometry, reason = null)
                            }
                        }
                        is MapShapeKind.SpatialObject -> {
                            val latest = spatialObjectHistoryRepository.getHistoryForObject(shape.id).firstOrNull()
                            if (latest != null) {
                                updateSpatialObjectGeometry(shape.id, latest.geometry, reason = null)
                            }
                        }
                        MapShapeKind.Area -> Unit
                    }
                    _uiState.update { it.copy(isSaving = false) }
                } catch (e: DomainError) {
                    Log.w(TAG, "Failed to undo edit for ${shape.id}", e)
                    _uiState.update { it.copy(isSaving = false, error = true) }
                }
            }
        }

        fun onErrorHandled() {
            _uiState.update { it.copy(error = false) }
        }

        private companion object {
            const val TAG = "GardenMapEditVM"
            const val MAP_EDIT_REASON = "Edited on map"
        }
    }
