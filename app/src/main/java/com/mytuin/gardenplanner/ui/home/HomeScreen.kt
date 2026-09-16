package com.mytuin.gardenplanner.ui.home

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
import com.mytuin.gardenplanner.data.database.GARDEN_DATABASE_VERSION

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.nav_home),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center),
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