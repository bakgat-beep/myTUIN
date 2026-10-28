package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpatialObjectsListScreen(
    onNavigateBack: () -> Unit,
    onCreateObject: () -> Unit,
    onObjectClick: (spatialObjectId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpatialObjectsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.spatial_objects_list_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_action),
                        )
                    }
                },
                actions = {
                    val ready = uiState as? SpatialObjectsListUiState.Ready
                    if (ready != null) {
                        IconButton(onClick = viewModel::toggleShowArchived) {
                            Icon(
                                imageVector =
                                    if (ready.showArchived) {
                                        Icons.Filled.VisibilityOff
                                    } else {
                                        Icons.Filled.Visibility
                                    },
                                contentDescription =
                                    stringResource(
                                        if (ready.showArchived) {
                                            R.string.spatial_objects_list_hide_archived
                                        } else {
                                            R.string.spatial_objects_list_show_archived
                                        },
                                    ),
                            )
                        }
                        IconButton(onClick = onCreateObject) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription =
                                    stringResource(R.string.spatial_objects_list_create_action),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        SpatialObjectsListContent(
            uiState = uiState,
            onCreateObject = onCreateObject,
            onObjectClick = onObjectClick,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun SpatialObjectsListContent(
    uiState: SpatialObjectsListUiState,
    onCreateObject: () -> Unit,
    onObjectClick: (spatialObjectId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        SpatialObjectsListUiState.Loading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        is SpatialObjectsListUiState.Ready ->
            if (uiState.objects.isEmpty()) {
                EmptyState(
                    title = stringResource(R.string.spatial_objects_list_empty_title),
                    description =
                        stringResource(
                            if (uiState.showArchived) {
                                R.string.spatial_objects_list_empty_description_show_archived
                            } else {
                                R.string.spatial_objects_list_empty_description
                            },
                        ),
                    icon = Icons.Filled.Yard,
                    actionLabel = stringResource(R.string.spatial_objects_list_empty_action),
                    onAction = onCreateObject,
                    modifier = modifier,
                )
            } else {
                SpatialObjectsList(
                    objects = uiState.objects,
                    onObjectClick = onObjectClick,
                    modifier = modifier,
                )
            }
        SpatialObjectsListUiState.Error ->
            ErrorState(
                message = stringResource(R.string.spatial_objects_list_error),
                modifier = modifier,
            )
    }
}

@Composable
private fun SpatialObjectsList(
    objects: List<SpatialObject>,
    onObjectClick: (spatialObjectId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(objects, key = { it.id }) { obj ->
            SpatialObjectRow(obj = obj, onClick = { onObjectClick(obj.id) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun SpatialObjectRow(
    obj: SpatialObject,
    onClick: () -> Unit,
) {
    val typeLabel = stringResource(infrastructureTypeLabelRes(obj.objectType))
    val displayName = spatialObjectDisplayName(obj.name, typeLabel)
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing.screenMargin, vertical = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (obj.name != null) {
            Text(
                text = typeLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = stringResource(statusLabelRes(obj.status)),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
