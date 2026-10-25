package com.mytuin.gardenplanner.ui.garden

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.model.location.Country
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.ui.theme.Spacing

private val LATITUDE_RANGE = -90.0..90.0
private val LONGITUDE_RANGE = -180.0..180.0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGardenLocationSheet(
    garden: Garden,
    countries: List<Country>,
    isSaving: Boolean,
    saveError: Boolean,
    onSave: (NewGardenLocation) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        EditGardenLocationContent(
            initialLocation = garden.toNewGardenLocation(),
            countries = countries,
            isSaving = isSaving,
            saveError = saveError,
            onSave = onSave,
            onCancel = onDismiss,
        )
    }
}

@Composable
fun EditGardenLocationContent(
    initialLocation: NewGardenLocation,
    countries: List<Country>,
    isSaving: Boolean,
    saveError: Boolean,
    onSave: (NewGardenLocation) -> Unit,
    onCancel: () -> Unit,
) {
    val formState = rememberLocationFormState(initialLocation)

    val onSubmit: () -> Unit = {
        formState.attempted = true
        if (formState.errors.isValid) {
            onSave(formState.toNewGardenLocation())
        }
    }

    if (formState.pickerOpen) {
        CountryPickerDialog(
            countries = countries,
            onSelect = { code ->
                formState.countryCode = code
                formState.pickerOpen = false
            },
            onDismiss = { formState.pickerOpen = false },
        )
    }

    LocationFormColumn(
        formState = formState,
        isSaving = isSaving,
        saveError = saveError,
        onSubmit = onSubmit,
        onCancel = onCancel,
    )
}

@Composable
private fun LocationFormColumn(
    formState: LocationFormState,
    isSaving: Boolean,
    saveError: Boolean,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        Text(
            text = stringResource(R.string.edit_location_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        LocationFields(
            formState = formState,
            enabled = !isSaving,
        )
        if (saveError) {
            Text(
                text = stringResource(R.string.edit_location_save_error_generic),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        SubmitRow(
            isSaving = isSaving,
            onSave = onSubmit,
            onCancel = onCancel,
        )
    }
}

@Composable
private fun LocationFields(
    formState: LocationFormState,
    enabled: Boolean,
) {
    CountryField(
        value = formState.countryCode,
        onChange = { formState.countryCode = it },
        isError = formState.errors.countryError,
        enabled = enabled,
        onPickCountry = { formState.pickerOpen = true },
    )
    StandardField(
        value = formState.region,
        onChange = { formState.region = it },
        labelRes = R.string.garden_detail_region_label,
        enabled = enabled,
    )
    StandardField(
        value = formState.locality,
        onChange = { formState.locality = it },
        labelRes = R.string.garden_detail_locality_label,
        enabled = enabled,
    )
    CoordinateField(
        value = formState.latitudeText,
        onChange = { formState.latitudeText = it },
        labelRes = R.string.garden_detail_latitude_label,
        isError = formState.errors.latitudeError,
        errorRes = R.string.edit_location_latitude_error,
        enabled = enabled,
    )
    CoordinateField(
        value = formState.longitudeText,
        onChange = { formState.longitudeText = it },
        labelRes = R.string.garden_detail_longitude_label,
        isError = formState.errors.longitudeError,
        errorRes = R.string.edit_location_longitude_error,
        enabled = enabled,
    )
    StandardField(
        value = formState.timezone,
        onChange = { formState.timezone = it },
        labelRes = R.string.garden_detail_timezone_label,
        enabled = enabled,
    )
    HemisphereSection(
        selected = formState.hemisphere,
        enabled = enabled,
        onSelect = { formState.hemisphere = it },
    )
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
private fun CoordinateField(
    value: String,
    onChange: (String) -> Unit,
    @StringRes labelRes: Int,
    isError: Boolean,
    @StringRes errorRes: Int,
    enabled: Boolean,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(labelRes)) },
        isError = isError,
        supportingText =
            if (isError) {
                { Text(stringResource(errorRes)) }
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
private fun CountryField(
    value: String,
    onChange: (String) -> Unit,
    isError: Boolean,
    enabled: Boolean,
    onPickCountry: () -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(R.string.garden_detail_country_label)) },
        isError = isError,
        supportingText =
            if (isError) {
                { Text(stringResource(R.string.edit_location_country_error)) }
            } else {
                null
            },
        singleLine = true,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
    )
    TextButton(
        onClick = onPickCountry,
        enabled = enabled,
    ) {
        Text(stringResource(R.string.edit_location_pick_country_action))
    }
}

@Composable
private fun HemisphereSection(
    selected: Hemisphere,
    enabled: Boolean,
    onSelect: (Hemisphere) -> Unit,
) {
    Text(
        text = stringResource(R.string.garden_detail_hemisphere_label),
        style = MaterialTheme.typography.labelLarge,
    )
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        Hemisphere.entries.forEach { option ->
            TextButton(
                onClick = { onSelect(option) },
                enabled = enabled && option != selected,
            ) {
                Text(
                    text =
                        stringResource(hemisphereLabelRes(option)) +
                            if (option == selected) " ✓" else "",
                )
            }
        }
    }
}

@Composable
private fun SubmitRow(
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
            Text(stringResource(R.string.edit_location_cancel))
        }
        Button(onClick = onSave, enabled = !isSaving) {
            Text(stringResource(R.string.edit_location_save))
        }
    }
}

@Composable
private fun CountryPickerDialog(
    countries: List<Country>,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val filtered =
        if (query.isBlank()) {
            countries
        } else {
            countries.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.code.equals(query, ignoreCase = true)
            }
        }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.country_picker_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text(stringResource(R.string.country_picker_search_hint)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(filtered, key = { it.code }) { country ->
                        TextButton(
                            onClick = { onSelect(country.code) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("${country.name} (${country.code})")
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.edit_location_cancel))
            }
        },
    )
}

@Stable
private class LocationFormState(
    initialLocation: NewGardenLocation,
    initialAttempted: Boolean,
    initialPickerOpen: Boolean,
) {
    var countryCode by mutableStateOf(initialLocation.countryCode ?: "")
    var region by mutableStateOf(initialLocation.region ?: "")
    var locality by mutableStateOf(initialLocation.locality ?: "")
    var latitudeText by mutableStateOf(initialLocation.latitude?.toString() ?: "")
    var longitudeText by mutableStateOf(initialLocation.longitude?.toString() ?: "")
    var timezone by mutableStateOf(initialLocation.timezone ?: "")
    var hemisphere by mutableStateOf(initialLocation.hemisphere)
    var attempted by mutableStateOf(initialAttempted)
    var pickerOpen by mutableStateOf(initialPickerOpen)

    val errors: LocationErrors
        get() =
            if (attempted) {
                validateLocation(countryCode, latitudeText, longitudeText)
            } else {
                LocationErrors.NONE
            }

    fun toNewGardenLocation(): NewGardenLocation =
        NewGardenLocation(
            countryCode = countryCode.trim().ifBlank { null },
            region = region.trim().ifBlank { null },
            locality = locality.trim().ifBlank { null },
            latitude = latitudeText.trim().toDoubleOrNull(),
            longitude = longitudeText.trim().toDoubleOrNull(),
            timezone = timezone.trim().ifBlank { null },
            hemisphere = hemisphere,
        )
}

private val LocationFormStateSaver =
    listSaver<LocationFormState, Any?>(
        save = {
            listOf(
                it.countryCode,
                it.region,
                it.locality,
                it.latitudeText,
                it.longitudeText,
                it.timezone,
                it.hemisphere.id,
                it.attempted,
                it.pickerOpen,
            )
        },
        restore = {
            // Saved values are the form field strings (empty when
            // unset). Passing them through NewGardenLocation preserves
            // the empty-versus-null distinction that toNewGardenLocation
            // normalises on save.
            LocationFormState(
                initialLocation =
                    NewGardenLocation(
                        countryCode = it[0] as String,
                        region = it[1] as String,
                        locality = it[2] as String,
                        latitude = (it[3] as String).toDoubleOrNull(),
                        longitude = (it[4] as String).toDoubleOrNull(),
                        timezone = it[5] as String,
                        hemisphere = Hemisphere.fromId(it[6] as String) ?: Hemisphere.UNKNOWN,
                    ),
                initialAttempted = it[7] as Boolean,
                initialPickerOpen = it[8] as Boolean,
            )
        },
    )

@Composable
private fun rememberLocationFormState(initialLocation: NewGardenLocation): LocationFormState =
    rememberSaveable(saver = LocationFormStateSaver) {
        LocationFormState(
            initialLocation = initialLocation,
            initialAttempted = false,
            initialPickerOpen = false,
        )
    }

private data class LocationErrors(
    val countryError: Boolean = false,
    val latitudeError: Boolean = false,
    val longitudeError: Boolean = false,
) {
    val isValid: Boolean
        get() = !countryError && !latitudeError && !longitudeError

    companion object {
        val NONE = LocationErrors()
    }
}

private fun validateLocation(
    countryCode: String,
    latitudeText: String,
    longitudeText: String,
): LocationErrors {
    val lat = latitudeText.trim().toDoubleOrNull()
    val lon = longitudeText.trim().toDoubleOrNull()
    return LocationErrors(
        countryError = !isValidCountryCode(countryCode),
        latitudeError = latitudeText.isNotBlank() && (lat == null || lat !in LATITUDE_RANGE),
        longitudeError = longitudeText.isNotBlank() && (lon == null || lon !in LONGITUDE_RANGE),
    )
}

private fun isValidCountryCode(code: String): Boolean {
    val trimmed = code.trim()
    if (trimmed.isEmpty()) return true
    return trimmed.length == 2 && trimmed.all { it in 'A'..'Z' || it in 'a'..'z' }
}

private fun hemisphereLabelRes(hemisphere: Hemisphere): Int =
    when (hemisphere) {
        Hemisphere.NORTHERN -> R.string.hemisphere_northern
        Hemisphere.SOUTHERN -> R.string.hemisphere_southern
        Hemisphere.EQUATORIAL -> R.string.hemisphere_equatorial
        Hemisphere.GLOBAL -> R.string.hemisphere_global
        Hemisphere.UNKNOWN -> R.string.hemisphere_unknown
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
