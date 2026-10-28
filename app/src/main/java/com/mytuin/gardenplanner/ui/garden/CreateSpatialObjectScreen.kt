package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSpatialObjectScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateSpatialObjectViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val areas by viewModel.areas.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.savedObjectId) {
        if (uiState.savedObjectId != null) {
            viewModel.onSavedHandled()
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.create_spatial_object_title)) },
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
        CreateSpatialObjectContent(
            seed = SpatialObjectFormSeed(),
            mode = SpatialObjectFormMode.CREATE,
            areas = areas,
            isSaving = uiState.isSaving,
            saveError = uiState.saveError,
            onSave = viewModel::save,
            onCancel = onNavigateBack,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun CreateSpatialObjectContent(
    seed: SpatialObjectFormSeed,
    mode: SpatialObjectFormMode,
    areas: List<Area>,
    isSaving: Boolean,
    saveError: Boolean,
    onSave: (SpatialObjectFormData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val formState = rememberSpatialObjectFormState(mode = mode, seed = seed)

    val handleSave: () -> Unit = {
        formState.attempted = true
        formState.toFormData()?.let(onSave)
    }

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
            mode = mode,
            areas = areas,
            currentGeometryTypeLabel = null,
        )
        if (saveError) {
            Text(
                text = stringResource(R.string.create_spatial_object_save_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        SpatialObjectFormActions(
            isSaving = isSaving,
            onSave = handleSave,
            onCancel = onCancel,
        )
    }
}
