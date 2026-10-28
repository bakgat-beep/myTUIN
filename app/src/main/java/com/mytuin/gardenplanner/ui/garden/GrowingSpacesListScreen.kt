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
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowingSpacesListScreen(
    onNavigateBack: () -> Unit,
    onCreateSpace: () -> Unit,
    onSpaceClick: (growingSpaceId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GrowingSpacesListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.growing_spaces_list_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_action),
                        )
                    }
                },
                actions = {
                    if (uiState is GrowingSpacesListUiState.Ready) {
                        IconButton(onClick = onCreateSpace) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription =
                                    stringResource(R.string.growing_spaces_list_create_action),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        GrowingSpacesListContent(
            uiState = uiState,
            onCreateSpace = onCreateSpace,
            onSpaceClick = onSpaceClick,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun GrowingSpacesListContent(
    uiState: GrowingSpacesListUiState,
    onCreateSpace: () -> Unit,
    onSpaceClick: (growingSpaceId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        GrowingSpacesListUiState.Loading ->
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        GrowingSpacesListUiState.NoSpaces ->
            EmptyState(
                title = stringResource(R.string.growing_spaces_list_empty_title),
                description = stringResource(R.string.growing_spaces_list_empty_description),
                icon = Icons.Filled.Yard,
                actionLabel = stringResource(R.string.growing_spaces_list_empty_action),
                onAction = onCreateSpace,
                modifier = modifier,
            )
        is GrowingSpacesListUiState.Ready ->
            GrowingSpacesList(
                spaces = uiState.spaces,
                onSpaceClick = onSpaceClick,
                modifier = modifier,
            )
        GrowingSpacesListUiState.Error ->
            ErrorState(
                message = stringResource(R.string.growing_spaces_list_error),
                modifier = modifier,
            )
    }
}

@Composable
private fun GrowingSpacesList(
    spaces: List<GrowingSpace>,
    onSpaceClick: (growingSpaceId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(spaces, key = { it.id }) { space ->
            GrowingSpaceRow(space = space, onClick = { onSpaceClick(space.id) })
            HorizontalDivider()
        }
    }
}

@Composable
private fun GrowingSpaceRow(
    space: GrowingSpace,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = Spacing.screenMargin, vertical = Spacing.m),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            text = space.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = secondaryLine(space),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(statusLabelRes(space.status)),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun secondaryLine(space: GrowingSpace): String {
    val typeLabel = stringResource(spaceTypeLabelRes(space.spaceType))
    val dimensions = dimensionSummary(space)
    return if (dimensions.isBlank()) typeLabel else "$typeLabel · $dimensions"
}

private fun dimensionSummary(space: GrowingSpace): String {
    val length = space.lengthMetres
    val width = space.widthMetres
    return when {
        length != null && width != null ->
            "${formatDimension(length)} × ${formatDimension(width)} m"
        length != null -> "${formatDimension(length)} m"
        else -> ""
    }
}
