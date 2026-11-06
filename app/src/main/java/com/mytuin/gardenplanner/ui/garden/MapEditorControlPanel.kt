package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.theme.Spacing

private val CONTROL_PANEL_ELEVATION = 3.dp

/**
 * Action strip shown when a shape is selected (Step 9g).
 *
 * Two rows: a persistent mode toggle (Select / Draw), then
 * mode-specific actions. All six callbacks travel in a
 * MapEditorCallbacks wrapper so the composable stays under the
 * LongParameterList threshold.
 *
 * Sits at the top-centre of the map so it does not conflict with
 * the recentre FAB or the bottom navigation bar.
 */
@Composable
fun MapEditorControlPanel(
    mode: MapEditMode,
    isSaving: Boolean,
    canCloseRing: Boolean,
    callbacks: MapEditorCallbacks,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = CONTROL_PANEL_ELEVATION,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.m, vertical = Spacing.xs),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            ModeToggle(
                currentMode = mode,
                enabled = !isSaving,
                onModeChange = callbacks.onModeChange,
            )
            when (mode) {
                MapEditMode.SELECT ->
                    SelectActions(
                        isSaving = isSaving,
                        onDelete = callbacks.onDelete,
                        onUndo = callbacks.onUndo,
                    )
                MapEditMode.DRAW ->
                    DrawActions(
                        isSaving = isSaving,
                        canCloseRing = canCloseRing,
                        onCloseRing = callbacks.onCloseRing,
                        onClearVertices = callbacks.onClearVertices,
                        onCancelDraw = callbacks.onCancelDraw,
                    )
            }
        }
    }
}

data class MapEditorCallbacks(
    val onModeChange: (MapEditMode) -> Unit,
    val onDelete: () -> Unit,
    val onUndo: () -> Unit,
    val onCloseRing: () -> Unit,
    val onClearVertices: () -> Unit,
    val onCancelDraw: () -> Unit,
)

@Composable
private fun ModeToggle(
    currentMode: MapEditMode,
    enabled: Boolean,
    onModeChange: (MapEditMode) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilterChip(
            selected = currentMode == MapEditMode.SELECT,
            onClick = { onModeChange(MapEditMode.SELECT) },
            label = { Text(stringResource(R.string.map_editor_mode_select)) },
            enabled = enabled,
        )
        FilterChip(
            selected = currentMode == MapEditMode.DRAW,
            onClick = { onModeChange(MapEditMode.DRAW) },
            label = { Text(stringResource(R.string.map_editor_mode_draw)) },
            enabled = enabled,
        )
    }
}

@Composable
private fun SelectActions(
    isSaving: Boolean,
    onDelete: () -> Unit,
    onUndo: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = onDelete, enabled = !isSaving) {
            Text(stringResource(R.string.map_editor_delete_action))
        }
        TextButton(onClick = onUndo, enabled = !isSaving) {
            Text(stringResource(R.string.map_editor_undo_action))
        }
    }
}

@Composable
private fun DrawActions(
    isSaving: Boolean,
    canCloseRing: Boolean,
    onCloseRing: () -> Unit,
    onClearVertices: () -> Unit,
    onCancelDraw: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onCloseRing,
            enabled = !isSaving && canCloseRing,
        ) {
            Text(stringResource(R.string.map_editor_close_ring_action))
        }
        TextButton(
            onClick = onClearVertices,
            enabled = !isSaving,
        ) {
            Text(stringResource(R.string.map_editor_clear_vertices_action))
        }
        TextButton(onClick = onCancelDraw, enabled = !isSaving) {
            Text(stringResource(R.string.map_editor_cancel_draw_action))
        }
    }
}
