package com.mytuin.gardenplanner.ui.plants

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.components.EmptyState

@Composable
fun PlantsScreen(modifier: Modifier = Modifier) {
    EmptyState(
        title = stringResource(R.string.empty_plants_title),
        description = stringResource(R.string.empty_plants_description),
        icon = Icons.Filled.Eco,
        modifier = modifier.fillMaxSize(),
    )
}