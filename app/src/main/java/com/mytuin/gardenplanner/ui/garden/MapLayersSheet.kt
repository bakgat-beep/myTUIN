package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.ui.theme.Spacing

/**
 * Bottom sheet containing the layer toggles and per-type filters
 * (Step 9h).
 *
 * Reads `filterState` from the caller; every action goes through
 * `callbacks`. The sheet itself holds no state beyond the sheet's
 * animation state — no local copy of the filter state to drift from
 * the ViewModel's.
 *
 * The "Reset all" button is enabled when any filter is non-default.
 * Reset only clears filters; it does not close the sheet (Q6).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MapLayersSheet(
    filterState: MapFilterState,
    onDismiss: () -> Unit,
    onNavigateToHistory: () -> Unit,
    callbacks: MapLayersCallbacks,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.screenMargin)
                    .padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.m),
        ) {
            Text(
                text = stringResource(R.string.map_layers_title),
                style = MaterialTheme.typography.titleLarge,
            )
            LayersSection(filterState = filterState, callbacks = callbacks)
            GrowingSpaceTypesSection(filterState = filterState, callbacks = callbacks)
            InfrastructureTypesSection(filterState = filterState, callbacks = callbacks)
            ResetRow(filterState = filterState, callbacks = callbacks)
            HistoryRow(onNavigateToHistory = onNavigateToHistory, onDismiss = onDismiss)
        }
    }
}

data class MapLayersCallbacks(
    val onShowAreasChange: (Boolean) -> Unit,
    val onShowGrowingSpacesChange: (Boolean) -> Unit,
    val onShowSpatialObjectsChange: (Boolean) -> Unit,
    val onToggleGrowingSpaceType: (GrowingSpaceType) -> Unit,
    val onToggleInfrastructureType: (InfrastructureType) -> Unit,
    val onReset: () -> Unit,
)

@Composable
private fun LayersSection(
    filterState: MapFilterState,
    callbacks: MapLayersCallbacks,
) {
    Text(
        text = stringResource(R.string.map_layers_kinds_header),
        style = MaterialTheme.typography.titleMedium,
    )
    LayerSwitch(
        label = stringResource(R.string.map_layers_kind_areas),
        checked = filterState.showAreas,
        onCheckedChange = callbacks.onShowAreasChange,
    )
    LayerSwitch(
        label = stringResource(R.string.map_layers_kind_growing_spaces),
        checked = filterState.showGrowingSpaces,
        onCheckedChange = callbacks.onShowGrowingSpacesChange,
    )
    LayerSwitch(
        label = stringResource(R.string.map_layers_kind_spatial_objects),
        checked = filterState.showSpatialObjects,
        onCheckedChange = callbacks.onShowSpatialObjectsChange,
    )
}

@Composable
private fun LayerSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GrowingSpaceTypesSection(
    filterState: MapFilterState,
    callbacks: MapLayersCallbacks,
) {
    Text(
        text = stringResource(R.string.map_layers_growing_space_types_header),
        style = MaterialTheme.typography.titleMedium,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        GrowingSpaceType.entries.forEach { type ->
            FilterChip(
                selected = type !in filterState.hiddenGrowingSpaceTypes,
                onClick = { callbacks.onToggleGrowingSpaceType(type) },
                label = { Text(stringResource(spaceTypeLabelRes(type))) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun InfrastructureTypesSection(
    filterState: MapFilterState,
    callbacks: MapLayersCallbacks,
) {
    Text(
        text = stringResource(R.string.map_layers_spatial_object_types_header),
        style = MaterialTheme.typography.titleMedium,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        InfrastructureType.entries.forEach { type ->
            FilterChip(
                selected = type !in filterState.hiddenInfrastructureTypes,
                onClick = { callbacks.onToggleInfrastructureType(type) },
                label = { Text(stringResource(infrastructureTypeLabelRes(type))) },
            )
        }
    }
}

@Composable
private fun ResetRow(
    filterState: MapFilterState,
    callbacks: MapLayersCallbacks,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            onClick = callbacks.onReset,
            enabled = !filterState.isDefault,
        ) {
            Text(stringResource(R.string.map_layers_reset_action))
        }
    }
}

@Composable
private fun HistoryRow(
    onNavigateToHistory: () -> Unit,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        TextButton(
            onClick = {
                onDismiss()
                onNavigateToHistory()
            },
        ) {
            Text(stringResource(R.string.map_layers_history_action))
        }
    }
}
