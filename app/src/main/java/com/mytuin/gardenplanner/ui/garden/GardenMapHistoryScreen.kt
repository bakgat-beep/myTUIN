package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.components.ErrorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenMapHistoryScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GardenMapHistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.garden_history_title)) },
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
        GardenMapHistoryContent(
            uiState = uiState,
            onTimeChange = viewModel::setTime,
            onJumpToNow = viewModel::jumpToNow,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun GardenMapHistoryContent(
    uiState: GardenMapHistoryUiState,
    onTimeChange: (Long) -> Unit,
    onJumpToNow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        GardenMapHistoryUiState.Loading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        GardenMapHistoryUiState.NoGarden ->
            EmptyState(
                title = stringResource(R.string.garden_history_no_garden_title),
                description = stringResource(R.string.garden_history_no_garden_description),
                icon = Icons.Filled.Yard,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapHistoryUiState.NoShapes ->
            EmptyState(
                title = stringResource(R.string.garden_history_no_shapes_title),
                description = stringResource(R.string.garden_history_no_shapes_description),
                icon = Icons.Filled.Yard,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapHistoryUiState.NoHistory ->
            EmptyState(
                title = stringResource(R.string.garden_history_no_history_title),
                description = stringResource(R.string.garden_history_no_history_description),
                icon = Icons.Filled.History,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapHistoryUiState.Error ->
            ErrorState(
                message = stringResource(R.string.garden_history_error),
                modifier = modifier,
            )
        is GardenMapHistoryUiState.Ready ->
            ReadyHistory(
                uiState = uiState,
                onTimeChange = onTimeChange,
                onJumpToNow = onJumpToNow,
                modifier = modifier,
            )
    }
}

@Composable
private fun ReadyHistory(
    uiState: GardenMapHistoryUiState.Ready,
    onTimeChange: (Long) -> Unit,
    onJumpToNow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val handle = rememberShapesCanvasHandle()
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            GardenShapesCanvas(
                handle = handle,
                shapes = uiState.shapes,
                viewport = uiState.viewport,
                origin = uiState.origin,
                modifier = Modifier.fillMaxSize(),
            )
        }
        HistorySlider(
            currentTime = uiState.currentTime,
            minTime = uiState.timeline.minTime,
            maxTime = uiState.timeline.maxTime,
            onTimeChange = onTimeChange,
            onJumpToNow = onJumpToNow,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
