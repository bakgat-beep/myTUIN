package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.components.ErrorState

@Composable
fun GardenMapScreen(
    uiState: GardenMapUiState,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        GardenMapUiState.Loading ->
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        GardenMapUiState.NoGarden ->
            EmptyState(
                title = stringResource(R.string.garden_map_no_garden_title),
                description = stringResource(R.string.garden_map_no_garden_description),
                icon = Icons.Filled.Yard,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapUiState.NoGeometries ->
            EmptyState(
                title = stringResource(R.string.garden_map_no_geometries_title),
                description = stringResource(R.string.garden_map_no_geometries_description),
                icon = Icons.Filled.Yard,
                modifier = modifier.fillMaxSize(),
            )
        GardenMapUiState.Error ->
            ErrorState(
                message = stringResource(R.string.garden_map_error),
                modifier = modifier,
            )
        is GardenMapUiState.Ready ->
            GardenMapCanvas(
                uiState = uiState,
                modifier = modifier.fillMaxSize(),
            )
    }
}
