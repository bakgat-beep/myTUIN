package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.BuildConfig
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.garden.debug.MapLibrePoc

/**
 * A148=a: the MapLibre proof of concept replaces the Garden tab in
 * debug builds only. Release builds keep the placeholder until the
 * real Garden screen lands. When it does, this conditional and the
 * entire ui/garden/debug/ package are deleted.
 */
@Composable
fun GardenScreen(modifier: Modifier = Modifier) {
    if (BuildConfig.DEBUG) {
        MapLibrePoc(modifier = modifier)
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.nav_garden),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}