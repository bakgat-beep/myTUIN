package com.mytuin.gardenplanner.ui.planner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.components.EmptyState

@Composable
fun PlannerScreen(modifier: Modifier = Modifier) {
    EmptyState(
        title = stringResource(R.string.empty_planner_title),
        description = stringResource(R.string.empty_planner_description),
        icon = Icons.Filled.EditCalendar,
        modifier = modifier.fillMaxSize(),
    )
}
