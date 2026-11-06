package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@Composable
fun GardenMapScreen(
    uiState: GardenMapUiState,
    filterState: MapFilterState,
    editState: GardenMapEditUiState,
    callbacks: GardenMapScreenCallbacks,
    modifier: Modifier = Modifier,
) {
    var showLayersSheet by rememberSaveable { mutableStateOf(false) }

    when (uiState) {
        GardenMapUiState.Loading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        GardenMapUiState.NoGarden ->
            EmptyState(
                title = stringResource(R.string.garden_map_no_garden_title),
                description = stringResource(R.string.garden_map_no_garden_description),
                icon = Icons.Filled.Yard,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapUiState.NoGeometries ->
            EmptyState(
                title = stringResource(R.string.garden_map_no_geometries_title),
                description = stringResource(R.string.garden_map_no_geometries_description),
                icon = Icons.Filled.Yard,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapUiState.AllLayersHidden ->
            EmptyState(
                title = stringResource(R.string.map_layers_all_hidden_title),
                description = stringResource(R.string.map_layers_all_hidden_description),
                icon = Icons.Filled.Layers,
                actionLabel = stringResource(R.string.map_layers_reset_action),
                onAction = callbacks.filterCallbacks.onReset,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapUiState.Error ->
            ErrorState(
                message = stringResource(R.string.garden_map_error),
                modifier = modifier,
            )
        is GardenMapUiState.Ready ->
            ReadyMap(
                uiState = uiState,
                filterState = filterState,
                editState = editState,
                callbacks = callbacks,
                onOpenLayers = { showLayersSheet = true },
                modifier = modifier.fillMaxSize(),
            )
    }

    if (showLayersSheet) {
        MapLayersSheet(
            filterState = filterState,
            onDismiss = { showLayersSheet = false },
            onNavigateToHistory = callbacks.onNavigateToHistory,
            callbacks = callbacks.filterCallbacks,
        )
    }
}

data class GardenMapScreenCallbacks(
    val onSelectShape: (String) -> Unit,
    val onClearSelection: () -> Unit,
    val onSaveGeometry: (MapShape, Geometry) -> Unit,
    val onArchiveSelected: (MapShape) -> Unit,
    val onUndoSelected: (MapShape) -> Unit,
    val onNavigateToHistory: () -> Unit,
    val filterCallbacks: MapLayersCallbacks,
)

@Composable
private fun ReadyMap(
    uiState: GardenMapUiState.Ready,
    filterState: MapFilterState,
    editState: GardenMapEditUiState,
    callbacks: GardenMapScreenCallbacks,
    onOpenLayers: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        GardenMapCanvas(
            uiState = uiState,
            editState = editState,
            onSelectShape = callbacks.onSelectShape,
            onClearSelection = callbacks.onClearSelection,
            onSaveGeometry = callbacks.onSaveGeometry,
            onArchiveSelected = callbacks.onArchiveSelected,
            onUndoSelected = callbacks.onUndoSelected,
            modifier = Modifier.fillMaxSize(),
        )
        MapLayersFab(
            showBadge = !filterState.isDefault,
            onClick = onOpenLayers,
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(Spacing.l),
        )
    }
}
