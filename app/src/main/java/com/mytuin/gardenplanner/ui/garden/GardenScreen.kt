package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GardenScreen(
    modifier: Modifier = Modifier,
    viewModel: GardenMapViewModel = hiltViewModel(),
    editViewModel: GardenMapEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editState by editViewModel.uiState.collectAsStateWithLifecycle()
    GardenMapScreen(
        uiState = uiState,
        editState = editState,
        onSelectShape = editViewModel::selectShape,
        onClearSelection = editViewModel::clearSelection,
        onSaveGeometry = editViewModel::saveGeometry,
        onArchiveSelected = editViewModel::archiveSelected,
        onUndoSelected = editViewModel::undoLastEdit,
        modifier = modifier,
    )
}
