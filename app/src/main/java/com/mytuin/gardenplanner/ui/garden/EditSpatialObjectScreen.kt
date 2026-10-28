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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSpatialObjectScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditSpatialObjectViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val areas by viewModel.areas.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.savedEvent) {
        if (uiState.savedEvent) {
            viewModel.onSavedHandled()
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_spatial_object_title)) },
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
    ) { innerPadding ->
        EditSpatialObjectBody(
            uiState = uiState,
            areas = areas,
            onSave = viewModel::save,
            onCancel = onNavigateBack,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun EditSpatialObjectBody(
    uiState: EditSpatialObjectUiState,
    areas: List<Area>,
    onSave: (SpatialObjectFormData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spatialObject = uiState.spatialObject
    when {
        uiState.isLoading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        uiState.loadError || spatialObject == null ->
            ErrorState(
                message = stringResource(R.string.edit_spatial_object_load_error),
                modifier = modifier,
            )
        else ->
            EditSpatialObjectLoaded(
                spatialObject = spatialObject,
                areas = areas,
                isSaving = uiState.isSaving,
                saveError = uiState.saveError,
                onSave = onSave,
                onCancel = onCancel,
                modifier = modifier,
            )
    }
}

@Composable
private fun EditSpatialObjectLoaded(
    spatialObject: SpatialObject,
    areas: List<Area>,
    isSaving: Boolean,
    saveError: Boolean,
    onSave: (SpatialObjectFormData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val seed = SpatialObjectFormSeed.fromObject(spatialObject)
    val formState =
        rememberSpatialObjectFormState(
            mode = SpatialObjectFormMode.EDIT,
            seed = seed,
        )
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        SpatialObjectFormFields(
            formState = formState,
            enabled = !isSaving,
            mode = SpatialObjectFormMode.EDIT,
            areas = areas,
            currentGeometryTypeLabel = stringResource(geometryTypeLabelRes(spatialObject.geometry.geometryType)),
        )
        if (saveError) {
            Text(
                text = stringResource(R.string.edit_spatial_object_save_error),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
            )
        }
        SpatialObjectFormActions(
            isSaving = isSaving,
            onSave = {
                formState.attempted = true
                formState.toFormData()?.let(onSave)
            },
            onCancel = onCancel,
        )
    }
}

@androidx.annotation.StringRes
private fun geometryTypeLabelRes(type: com.mytuin.gardenplanner.domain.vocabulary.GeometryType): Int =
    when (type) {
        com.mytuin.gardenplanner.domain.vocabulary.GeometryType.POINT -> R.string.growing_space_detail_geometry_point
        com.mytuin.gardenplanner.domain.vocabulary.GeometryType.LINE -> R.string.growing_space_detail_geometry_line
        com.mytuin.gardenplanner.domain.vocabulary.GeometryType.POLYGON -> R.string.growing_space_detail_geometry_polygon
    }
