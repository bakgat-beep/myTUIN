package com.mytuin.gardenplanner.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.BuildConfig
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.data.database.GARDEN_DATABASE_VERSION
import com.mytuin.gardenplanner.ui.components.EmptyState

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        EmptyState(
            title = stringResource(R.string.empty_home_title),
            description = stringResource(R.string.empty_home_description),
            icon = Icons.Filled.Home,
        )
        if (BuildConfig.DEBUG) {
            DiagnosticsFooter(
                appVersion = BuildConfig.VERSION_NAME,
                schemaVersion = GARDEN_DATABASE_VERSION,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
