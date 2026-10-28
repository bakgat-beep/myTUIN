package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToGrowingSpaces: () -> Unit,
    onNavigateToSpatialObjects: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GardenDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LocationSavedSnackbar(
        savedEvent = uiState.savedEvent,
        snackbarHostState = snackbarHostState,
        onHandled = viewModel::onSavedEventHandled,
        onUndo = viewModel::undoLocation,
    )

    EditLocationSheetIfOpen(
        uiState = uiState,
        onSave = viewModel::saveLocation,
        onDismiss = viewModel::closeEditSheet,
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            uiState.garden?.name
                                ?: stringResource(R.string.nav_garden),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_action),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        GardenDetailContent(
            uiState = uiState,
            onEditLocation = viewModel::openEditSheet,
            onNavigateToGrowingSpaces = onNavigateToGrowingSpaces,
            onNavigateToSpatialObjects = onNavigateToSpatialObjects,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun LocationSavedSnackbar(
    savedEvent: GardenDetailUiState.SavedEvent?,
    snackbarHostState: SnackbarHostState,
    onHandled: () -> Unit,
    onUndo: () -> Unit,
) {
    val locationUpdatedMessage = stringResource(R.string.location_updated_message)
    val undoLabel = stringResource(R.string.undo_action)
    LaunchedEffect(savedEvent) {
        if (savedEvent == null) return@LaunchedEffect
        onHandled()
        val result =
            snackbarHostState.showSnackbar(
                message = locationUpdatedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short,
            )
        if (result == SnackbarResult.ActionPerformed) {
            onUndo()
        }
    }
}

@Composable
private fun EditLocationSheetIfOpen(
    uiState: GardenDetailUiState,
    onSave: (NewGardenLocation) -> Unit,
    onDismiss: () -> Unit,
) {
    val garden = uiState.garden
    if (uiState.isEditSheetOpen && garden != null) {
        EditGardenLocationSheet(
            garden = garden,
            countries = uiState.countries,
            isSaving = uiState.isEditSaving,
            saveError = uiState.editSaveError,
            onSave = onSave,
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun GardenDetailContent(
    uiState: GardenDetailUiState,
    onEditLocation: () -> Unit,
    onNavigateToGrowingSpaces: () -> Unit,
    onNavigateToSpatialObjects: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading ->
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        uiState.loadError || uiState.garden == null ->
            ErrorState(
                message = stringResource(R.string.garden_detail_load_error),
                modifier = modifier,
            )
        else ->
            LoadedGardenDetail(
                garden = uiState.garden,
                onEditLocation = onEditLocation,
                onNavigateToGrowingSpaces = onNavigateToGrowingSpaces,
                onNavigateToSpatialObjects = onNavigateToSpatialObjects,
                modifier = modifier,
            )
    }
}

@Composable
private fun LoadedGardenDetail(
    garden: Garden,
    onEditLocation: () -> Unit,
    onNavigateToGrowingSpaces: () -> Unit,
    onNavigateToSpatialObjects: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        if (!garden.description.isNullOrBlank()) {
            Text(
                text = garden.description,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        LocationSection(garden = garden, onEditLocation = onEditLocation)
        GrowingSpacesSection(onNavigateToGrowingSpaces = onNavigateToGrowingSpaces)
        SpatialObjectsSection(onNavigateToSpatialObjects = onNavigateToSpatialObjects)
    }
}

@Composable
private fun LocationSection(
    garden: Garden,
    onEditLocation: () -> Unit,
) {
    Text(
        text = stringResource(R.string.garden_detail_location_section),
        style = MaterialTheme.typography.titleMedium,
    )
    DetailField(
        label = stringResource(R.string.garden_detail_country_label),
        value = garden.countryCode,
    )
    DetailField(
        label = stringResource(R.string.garden_detail_region_label),
        value = garden.region,
    )
    DetailField(
        label = stringResource(R.string.garden_detail_locality_label),
        value = garden.locality,
    )
    DetailField(
        label = stringResource(R.string.garden_detail_latitude_label),
        value = garden.latitude?.toString(),
    )
    DetailField(
        label = stringResource(R.string.garden_detail_longitude_label),
        value = garden.longitude?.toString(),
    )
    DetailField(
        label = stringResource(R.string.garden_detail_timezone_label),
        value = garden.timezone,
    )
    DetailField(
        label = stringResource(R.string.garden_detail_hemisphere_label),
        value = stringResource(hemisphereLabelRes(garden.hemisphere)),
    )
    Button(onClick = onEditLocation) {
        Text(stringResource(R.string.garden_detail_edit_location_action))
    }
}

@Composable
private fun GrowingSpacesSection(onNavigateToGrowingSpaces: () -> Unit) {
    Text(
        text = stringResource(R.string.garden_detail_growing_spaces_section),
        style = MaterialTheme.typography.titleMedium,
    )
    Button(onClick = onNavigateToGrowingSpaces) {
        Text(stringResource(R.string.garden_detail_view_growing_spaces_action))
    }
}

@Composable
private fun SpatialObjectsSection(onNavigateToSpatialObjects: () -> Unit) {
    Text(
        text = stringResource(R.string.garden_detail_spatial_objects_section),
        style = MaterialTheme.typography.titleMedium,
    )
    Button(onClick = onNavigateToSpatialObjects) {
        Text(stringResource(R.string.garden_detail_view_spatial_objects_action))
    }
}

@Composable
private fun DetailField(
    label: String,
    value: String?,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value ?: stringResource(R.string.garden_detail_not_set),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun hemisphereLabelRes(hemisphere: Hemisphere): Int =
    when (hemisphere) {
        Hemisphere.NORTHERN -> R.string.hemisphere_northern
        Hemisphere.SOUTHERN -> R.string.hemisphere_southern
        Hemisphere.EQUATORIAL -> R.string.hemisphere_equatorial
        Hemisphere.GLOBAL -> R.string.hemisphere_global
        Hemisphere.UNKNOWN -> R.string.hemisphere_unknown
    }
