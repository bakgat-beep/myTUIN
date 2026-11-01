package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * The Garden tab. In 9f this is the production map; the debug-gated
 * MapLibrePoc that previously filled this screen is deleted.
 */
@Composable
fun GardenScreen(
    modifier: Modifier = Modifier,
    viewModel: GardenMapViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GardenMapScreen(uiState = uiState, modifier = modifier)
}
