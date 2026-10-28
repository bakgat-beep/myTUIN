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
import androidx.compose.material3.MaterialTheme
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
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGrowingSpaceScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditGrowingSpaceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                title = {
                    Text(
                        text =
                            uiState.space?.name
                                ?: stringResource(R.string.edit_growing_space_title),
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
    ) { innerPadding ->
        val space = uiState.space
        when {
            uiState.isLoading ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            uiState.loadError || space == null ->
                ErrorState(
                    message = stringResource(R.string.edit_growing_space_load_error),
                    modifier = Modifier.padding(innerPadding),
                )
            else ->
                EditGrowingSpaceContent(
                    space = space,
                    isSaving = uiState.isSaving,
                    saveError = uiState.saveError,
                    onSave = viewModel::save,
                    onCancel = onNavigateBack,
                    modifier = Modifier.padding(innerPadding),
                )
        }
    }
}

@Composable
fun EditGrowingSpaceContent(
    space: GrowingSpace,
    isSaving: Boolean,
    saveError: Boolean,
    onSave: (GrowingSpaceFormData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val seed = GrowingSpaceFormSeed.fromSpace(space)
    val formState = rememberGrowingSpaceFormState(seed)

    EditGrowingSpaceLayout(
        formState = formState,
        space = space,
        isSaving = isSaving,
        saveError = saveError,
        onSave = onSave,
        onCancel = onCancel,
        modifier = modifier,
    )
}

@Composable
private fun EditGrowingSpaceLayout(
    formState: GrowingSpaceFormState,
    space: GrowingSpace,
    isSaving: Boolean,
    saveError: Boolean,
    onSave: (GrowingSpaceFormData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        GrowingSpaceFormFields(
            formState = formState,
            enabled = !isSaving,
            mode = GrowingSpaceFormMode.EDIT,
            existingGeometryType = space.geometry?.geometryType,
        )
        if (saveError) {
            Text(
                text = stringResource(R.string.edit_growing_space_save_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        GrowingSpaceFormActions(
            isSaving = isSaving,
            onSave = handleSave,
            onCancel = onCancel,
        )
    }
}
