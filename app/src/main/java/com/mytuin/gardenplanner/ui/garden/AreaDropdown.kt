package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Area

/**
 * Nullable area picker (Step 9e).
 *
 * A SpatialObject may sit in no Area (B2). The dropdown therefore
 * offers a "No area" row plus one row per Area in the current
 * garden. The selection is the Area's id, or null for "No area".
 *
 * Areas are passed in already filtered to the current garden and
 * to the active set (or active-plus-archived, per the parent
 * screen's choice). This composable does no filtering.
 */
@Composable
fun AreaDropdown(
    areas: List<Area>,
    selectedAreaId: String?,
    onSelect: (String?) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.form_spatial_object_area_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { expanded = true },
                enabled = enabled,
            ) {
                Text(selectedAreaLabel(areas, selectedAreaId))
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.form_spatial_object_area_none)) },
                    onClick = {
                        onSelect(null)
                        expanded = false
                    },
                )
                areas.forEach { area ->
                    DropdownMenuItem(
                        text = { Text(area.name) },
                        onClick = {
                            onSelect(area.id)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun selectedAreaLabel(
    areas: List<Area>,
    selectedAreaId: String?,
): String {
    if (selectedAreaId == null) return stringResource(R.string.form_spatial_object_area_none)
    val area = areas.firstOrNull { it.id == selectedAreaId }
    return area?.name ?: stringResource(R.string.form_spatial_object_area_unknown)
}
