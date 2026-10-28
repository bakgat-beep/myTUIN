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
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.ui.theme.Spacing

@Composable
fun GrowingSpaceFormFields(
    formState: GrowingSpaceFormState,
    enabled: Boolean,
    mode: GrowingSpaceFormMode,
    existingGeometryType: GeometryType?,
) {
    val errors = formState.errors
    NameField(
        value = formState.name,
        onChange = { formState.name = it },
        isError = errors.nameBlank,
        enabled = enabled,
    )
    TypeDropdown(
        selected = formState.spaceType,
        enabled = enabled,
        onSelect = { formState.spaceType = it },
    )
    StandardField(
        value = formState.description,
        onChange = { formState.description = it },
        labelRes = R.string.form_growing_space_description_label,
        enabled = enabled,
    )
    DimensionsGrid(formState = formState, errors = errors, enabled = enabled)
    GeometrySection(
        formState = formState,
        isError = errors.geometryInvalid,
        enabled = enabled,
        mode = mode,
        existingGeometryType = existingGeometryType,
    )
    StandardField(
        value = formState.notes,
        onChange = { formState.notes = it },
        labelRes = R.string.form_growing_space_notes_label,
        enabled = enabled,
    )
}

@Composable
private fun NameField(
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(R.string.form_growing_space_name_label)) },
        isError = isError,
        supportingText =
            if (isError) {
                { Text(stringResource(R.string.form_growing_space_name_error_blank)) }
            } else {
                null
            },
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
private fun TypeDropdown(
    selected: GrowingSpaceType,
    enabled: Boolean,
    onSelect: (GrowingSpaceType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = stringResource(R.string.form_growing_space_type_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box {
            Button(
                onClick = { expanded = true },
                enabled = enabled,
            ) {
                Text(stringResource(spaceTypeLabelRes(selected)))
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                GrowingSpaceType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(stringResource(spaceTypeLabelRes(type))) },
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun DimensionsGrid(
    formState: GrowingSpaceFormState,
    errors: GrowingSpaceFormErrors,
    enabled: Boolean,
) {
    Text(
        text = stringResource(R.string.form_growing_space_dimensions_header),
        style = MaterialTheme.typography.titleMedium,
    )
    DimensionField(
        value = formState.lengthText,
        onChange = { formState.lengthText = it },
        labelRes = R.string.form_growing_space_length_label,
        isError = errors.lengthInvalid,
        enabled = enabled,
    )
    DimensionField(
        value = formState.widthText,
        onChange = { formState.widthText = it },
        labelRes = R.string.form_growing_space_width_label,
        isError = errors.widthInvalid,
        enabled = enabled,
    )
    DimensionField(
        value = formState.heightText,
        onChange = { formState.heightText = it },
        labelRes = R.string.form_growing_space_height_label,
        isError = errors.heightInvalid,
        enabled = enabled,
    )
    DimensionField(
        value = formState.diameterText,
        onChange = { formState.diameterText = it },
        labelRes = R.string.form_growing_space_diameter_label,
        isError = errors.diameterInvalid,
        enabled = enabled,
    )
    DimensionField(
        value = formState.areaText,
        onChange = { formState.areaText = it },
        labelRes = R.string.form_growing_space_area_label,
        isError = errors.areaInvalid,
        enabled = enabled,
    )
    DimensionField(
        value = formState.volumeText,
        onChange = { formState.volumeText = it },
        labelRes = R.string.form_growing_space_volume_label,
        isError = errors.volumeInvalid,
        enabled = enabled,
    )
}

@Composable
private fun DimensionField(
    value: String,
    onChange: (String) -> Unit,
    @StringRes labelRes: Int,
    isError: Boolean,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(labelRes)) },
        isError = isError,
        supportingText =
            if (isError) {
                { Text(stringResource(R.string.form_growing_space_dimension_error)) }
            } else {
                null
            },
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
private fun GeometrySection(
    formState: GrowingSpaceFormState,
    isError: Boolean,
    enabled: Boolean,
    mode: GrowingSpaceFormMode,
    existingGeometryType: GeometryType?,
) {
    GeometryHeader(mode = mode, existingGeometryType = existingGeometryType)
    RectangleFields(formState = formState, enabled = enabled)
    if (isError) {
        Text(
            text = stringResource(R.string.form_growing_space_geometry_error),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun GeometryHeader(
    mode: GrowingSpaceFormMode,
    existingGeometryType: GeometryType?,
) {
    Text(
        text = stringResource(R.string.form_growing_space_geometry_header),
        style = MaterialTheme.typography.titleMedium,
    )
    if (mode == GrowingSpaceFormMode.EDIT) {
        Text(
            text =
                stringResource(
                    R.string.form_growing_space_geometry_current_format,
                    stringResource(geometryTypeLabelRes(existingGeometryType)),
                ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    Text(
        text =
            stringResource(
                if (mode == GrowingSpaceFormMode.EDIT) {
                    R.string.form_growing_space_geometry_help_edit
                } else {
                    R.string.form_growing_space_geometry_help_create
                },
            ),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun RectangleFields(
    formState: GrowingSpaceFormState,
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
fun GrowingSpaceFormActions(
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

@StringRes
private fun geometryTypeLabelRes(type: GeometryType?): Int =
    when (type) {
        GeometryType.POINT -> R.string.growing_space_detail_geometry_point
        GeometryType.LINE -> R.string.growing_space_detail_geometry_line
        GeometryType.POLYGON -> R.string.growing_space_detail_geometry_polygon
        null -> R.string.growing_space_detail_geometry_not_set
    }
