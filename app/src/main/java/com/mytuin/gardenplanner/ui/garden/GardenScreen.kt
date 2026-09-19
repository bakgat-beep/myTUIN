package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Yard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.BuildConfig
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.garden.debug.MapLibrePoc

/**
 * A148=a: the MapLibre proof of concept replaces the Garden tab in
 * debug builds only. Release builds show the empty state until the
 * real Garden screen lands.
 */
@Composable
fun GardenScreen(modifier: Modifier = Modifier) {
    if (BuildConfig.DEBUG) {
        MapLibrePoc(modifier = modifier)
    } else {
        EmptyState(
            title = stringResource(R.string.empty_garden_title),
            description = stringResource(R.string.empty_garden_description),
            icon = Icons.Filled.Yard,
            modifier = modifier.fillMaxSize(),
        )
    }
}
