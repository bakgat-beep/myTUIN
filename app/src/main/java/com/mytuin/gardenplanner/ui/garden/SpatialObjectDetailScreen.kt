package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpatialObjectDetailScreen(
    onNavigateBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpatialObjectDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.finishedEvent) {
        if (uiState.finishedEvent != null) {
            viewModel.onFinishedHandled()
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val obj = uiState.spatialObject
                    Text(
                        text =
                            obj?.name?.takeIf { it.isNotBlank() }
                                ?: stringResource(R.string.spatial_object_detail_title),
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
        SpatialObjectDetailContent(
            uiState = uiState,
            onEdit = onEdit,
            onArchive = viewModel::archive,
            onRestore = viewModel::restore,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun SpatialObjectDetailContent(
    uiState: SpatialObjectDetailUiState,
    onEdit: () -> Unit,
    onArchive: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val obj = uiState.spatialObject
    when {
        uiState.isLoading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        uiState.loadError || obj == null ->
            ErrorState(
                message = stringResource(R.string.spatial_object_detail_load_error),
                modifier = modifier,
            )
        else ->
            LoadedSpatialObjectDetail(
                spatialObject = obj,
                onEdit = onEdit,
                onArchive = onArchive,
                onRestore = onRestore,
                modifier = modifier,
            )
    }
}

@Composable
private fun LoadedSpatialObjectDetail(
    spatialObject: SpatialObject,
    onEdit: () -> Unit,
    onArchive: () -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showArchiveConfirm by rememberSaveable { mutableStateOf(false) }

    if (showArchiveConfirm) {
        ArchiveConfirmDialog(
            onConfirm = {
                showArchiveConfirm = false
                onArchive()
            },
            onDismiss = { showArchiveConfirm = false },
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        Text(
            text =
                stringResource(
                    R.string.spatial_object_detail_type_and_status_format,
                    stringResource(infrastructureTypeLabelRes(spatialObject.objectType)),
                    stringResource(statusLabelRes(spatialObject.status)),
                ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        DescriptionSection(spatialObject)
        GeometrySection(spatialObject)
        NotesSection(spatialObject)
        DetailActions(
            spatialObject = spatialObject,
            onEdit = onEdit,
            onRequestArchive = { showArchiveConfirm = true },
            onRestore = onRestore,
        )
    }
}

@Composable
private fun DetailActions(
    spatialObject: SpatialObject,
    onEdit: () -> Unit,
    onRequestArchive: () -> Unit,
    onRestore: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(Spacing.m, Alignment.Start)) {
        Button(onClick = onEdit) {
            Text(stringResource(R.string.spatial_object_detail_edit_action))
        }
        if (spatialObject.status == RecordStatus.ARCHIVED) {
            OutlinedButton(onClick = onRestore) {
                Text(stringResource(R.string.spatial_object_detail_restore_action))
            }
        } else {
            OutlinedButton(onClick = onRequestArchive) {
                Text(stringResource(R.string.spatial_object_detail_archive_action))
            }
        }
    }
}

@Composable
private fun ArchiveConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.spatial_object_archive_confirm_title)) },
        text = { Text(stringResource(R.string.spatial_object_archive_confirm_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.spatial_object_archive_confirm_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.spatial_object_archive_confirm_cancel))
            }
        },
    )
}

@Composable
private fun DescriptionSection(spatialObject: SpatialObject) {
    val description = spatialObject.description
    if (description.isNullOrBlank()) return
    Text(
        text = stringResource(R.string.spatial_object_detail_section_description),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(text = description, style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun GeometrySection(spatialObject: SpatialObject) {
    Text(
        text = stringResource(R.string.spatial_object_detail_section_geometry),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text =
            stringResource(
                when (spatialObject.geometry.geometryType) {
                    com.mytuin.gardenplanner.domain.vocabulary.GeometryType.POINT -> R.string.growing_space_detail_geometry_point
                    com.mytuin.gardenplanner.domain.vocabulary.GeometryType.LINE -> R.string.growing_space_detail_geometry_line
                    com.mytuin.gardenplanner.domain.vocabulary.GeometryType.POLYGON -> R.string.growing_space_detail_geometry_polygon
                },
            ),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun NotesSection(spatialObject: SpatialObject) {
    val notes = spatialObject.notes
    if (notes.isNullOrBlank()) return
    Text(
        text = stringResource(R.string.spatial_object_detail_section_notes),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(text = notes, style = MaterialTheme.typography.bodyMedium)
}
