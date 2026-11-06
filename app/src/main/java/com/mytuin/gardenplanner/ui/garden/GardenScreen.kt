package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GardenScreen(
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GardenMapViewModel = hiltViewModel(),
    editViewModel: GardenMapEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val editState by editViewModel.uiState.collectAsStateWithLifecycle()

    val filterCallbacks =
        remember(viewModel) {
            MapLayersCallbacks(
                onShowAreasChange = viewModel::setShowAreas,
                onShowGrowingSpacesChange = viewModel::setShowGrowingSpaces,
                onShowSpatialObjectsChange = viewModel::setShowSpatialObjects,
                onToggleGrowingSpaceType = viewModel::toggleHiddenGrowingSpaceType,
                onToggleInfrastructureType = viewModel::toggleHiddenInfrastructureType,
                onReset = viewModel::resetFilters,
            )
        }
    val callbacks =
        remember(editViewModel, filterCallbacks, onNavigateToHistory) {
            GardenMapScreenCallbacks(
                onSelectShape = editViewModel::selectShape,
                onClearSelection = editViewModel::clearSelection,
                onSaveGeometry = editViewModel::saveGeometry,
                onArchiveSelected = editViewModel::archiveSelected,
                onUndoSelected = editViewModel::undoLastEdit,
                onNavigateToHistory = onNavigateToHistory,
                filterCallbacks = filterCallbacks,
            )
        }

    GardenMapScreen(
        uiState = uiState,
        filterState = filterState,
        editState = editState,
        callbacks = callbacks,
        modifier = modifier,
    )
}
