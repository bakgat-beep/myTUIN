package com.mytuin.gardenplanner.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.theme.Spacing

/**
 * Developer diagnostic footer.
 *
 * Shows the application version and database schema version. The
 * caller gates visibility on BuildConfig.DEBUG; the composable itself
 * is unconditional.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 5, §31 (Project):
 * "App version and schema version are set and visible in a
 * diagnostic surface."
 *
 * RELEASE_AND_VERSIONING §29: application version and schema version
 * are related but separate; both are shown here.
 *
 * Extracted from HomeScreen so it can be tested with literal values
 * (A83). When a Settings screen exists, this moves and stops being
 * debug-gated.
 */
@Composable
fun DiagnosticsFooter(
    appVersion: String,
    schemaVersion: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(
            R.string.diagnostics_format,
            appVersion,
            schemaVersion,
        ),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.l),
    )
}