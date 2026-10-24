package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.theme.Spacing

@Composable
fun CreateGardenScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateGardenViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.savedGardenId) {
        if (uiState.savedGardenId != null) {
            viewModel.onSaveHandled()
            onNavigateBack()
        }
    }

    CreateGardenContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onSave = viewModel::onSave,
        onCancel = onNavigateBack,
        modifier = modifier,
    )
}

@Composable
fun CreateGardenContent(
    uiState: CreateGardenUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        Text(
            text = stringResource(R.string.create_garden_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        GardenFormFields(
            uiState = uiState,
            onNameChange = onNameChange,
            onDescriptionChange = onDescriptionChange,
            onSave = onSave,
        )
        if (uiState.saveError != null) {
            SaveErrorMessage()
        }
        Spacer(Modifier.height(Spacing.s))
        FormActions(
            isSaving = uiState.isSaving,
            onSave = onSave,
            onCancel = onCancel,
        )
    }
}

@Composable
private fun GardenFormFields(
    uiState: CreateGardenUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    OutlinedTextField(
        value = uiState.name,
        onValueChange = onNameChange,
        label = { Text(stringResource(R.string.create_garden_name_label)) },
        isError = uiState.nameError != null,
        supportingText =
            uiState.nameError?.let {
                { Text(stringResource(R.string.create_garden_name_error_blank)) }
            },
        singleLine = true,
        enabled = !uiState.isSaving,
        keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next,
            ),
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = uiState.description,
        onValueChange = onDescriptionChange,
        label = { Text(stringResource(R.string.create_garden_description_label)) },
        enabled = !uiState.isSaving,
        minLines = 3,
        keyboardOptions =
            KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
        keyboardActions = KeyboardActions(onDone = { onSave() }),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun SaveErrorMessage() {
    Text(
        text = stringResource(R.string.create_garden_save_error_generic),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
private fun FormActions(
    isSaving: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.m, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onCancel,
            enabled = !isSaving,
        ) {
            Text(stringResource(R.string.create_garden_cancel))
        }
        Button(
            onClick = onSave,
            enabled = !isSaving,
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.height(Spacing.l),
                    strokeWidth = Spacing.xs / 2,
                )
            } else {
                Text(stringResource(R.string.create_garden_save))
            }
        }
    }
}
