package com.mytuin.gardenplanner.ui.garden

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.ui.theme.Spacing

@Composable
fun SpatialObjectFormFields(
    formState: SpatialObjectFormState,
    enabled: Boolean,
    mode: SpatialObjectFormMode,
    areas: List<Area>,
    currentGeometryTypeLabel: String?,
) {
    val errors = formState.errors
    TypeDropdown(
        selected = formState.objectType,
        enabled = enabled,
        onSelect = { formState.objectType = it },
    )
    StandardField(
        value = formState.name,
        onChange = { formState.name = it },
        labelRes = R.string.form_spatial_object_name_label,
        enabled = enabled,
    )
    StandardField(
        value = formState.description,
        onChange = { formState.description = it },
        labelRes = R.string.form_spatial_object_description_label,
        enabled = enabled,
    )
    AreaDropdown(
        areas = areas,
        selectedAreaId = formState.areaId,
        onSelect = { formState.areaId = it },
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
    )
    GeometrySection(
        formState = formState,
        mode = mode,
        isError = errors.geometryInvalid,
        enabled = enabled,
        currentGeometryTypeLabel = currentGeometryTypeLabel,
    )
    StandardField(
        value = formState.notes,
        onChange = { formState.notes = it },
        labelRes = R.string.form_spatial_object_notes_label,
        enabled = enabled,
    )
}

@Composable
private fun TypeDropdown(
    selected: InfrastructureType,
    enabled: Boolean,
    onSelect: (InfrastructureType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = stringResource(R.string.form_spatial_object_type_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box {
            Button(
                onClick = { expanded = true },
                enabled = enabled,
            ) {
                Text(stringResource(infrastructureTypeLabelRes(selected)))
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                InfrastructureType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(stringResource(infrastructureTypeLabelRes(type))) },
                        onClick = {
                            onSelect(type)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardField(
    value: String,
    onChange: (String) -> Unit,
    @StringRes labelRes: Int,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(labelRes)) },
        singleLine = true,
        enabled = enabled,
        keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun GeometrySection(
    formState: SpatialObjectFormState,
    mode: SpatialObjectFormMode,
    isError: Boolean,
    enabled: Boolean,
    currentGeometryTypeLabel: String?,
) {
    Text(
        text = stringResource(R.string.form_spatial_object_geometry_header),
        style = MaterialTheme.typography.titleMedium,
    )
    when (mode) {
        SpatialObjectFormMode.CREATE -> CreateGeometryHelp()
        SpatialObjectFormMode.EDIT ->
            EditGeometryNotice(currentGeometryTypeLabel = currentGeometryTypeLabel)
    }
    if (mode == SpatialObjectFormMode.CREATE) {
        RectangleFields(formState = formState, enabled = enabled)
        if (isError) {
            Text(
                text = stringResource(R.string.form_spatial_object_geometry_error),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun CreateGeometryHelp() {
    Text(
        text = stringResource(R.string.form_spatial_object_geometry_help_create),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun EditGeometryNotice(currentGeometryTypeLabel: String?) {
    Text(
        text =
            stringResource(
                R.string.form_spatial_object_geometry_current_format,
                currentGeometryTypeLabel ?: stringResource(R.string.growing_space_detail_geometry_not_set),
            ),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    Text(
        text = stringResource(R.string.form_spatial_object_geometry_help_edit),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun RectangleFields(
    formState: SpatialObjectFormState,
    enabled: Boolean,
) {
    RectField(
        value = formState.rectX1,
        onChange = { formState.rectX1 = it },
        labelRes = R.string.form_growing_space_geometry_x1_label,
        enabled = enabled,
    )
    RectField(
        value = formState.rectY1,
        onChange = { formState.rectY1 = it },
        labelRes = R.string.form_growing_space_geometry_y1_label,
        enabled = enabled,
    )
    RectField(
        value = formState.rectX2,
        onChange = { formState.rectX2 = it },
        labelRes = R.string.form_growing_space_geometry_x2_label,
        enabled = enabled,
    )
    RectField(
        value = formState.rectY2,
        onChange = { formState.rectY2 = it },
        labelRes = R.string.form_growing_space_geometry_y2_label,
        enabled = enabled,
    )
}

@Composable
private fun RectField(
    value: String,
    onChange: (String) -> Unit,
    @StringRes labelRes: Int,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(labelRes)) },
        singleLine = true,
        enabled = enabled,
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
            ),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun SpatialObjectFormActions(
    isSaving: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.m, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = onCancel, enabled = !isSaving) {
            Text(stringResource(R.string.form_action_cancel))
        }
        Button(onClick = onSave, enabled = !isSaving) {
            Text(stringResource(R.string.form_action_save))
        }
    }
}
