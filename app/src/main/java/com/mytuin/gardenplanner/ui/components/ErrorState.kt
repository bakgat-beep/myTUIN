package com.mytuin.gardenplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.theme.Spacing

/**
 * A user-facing error state.
 *
 * PHASE_0_PROJECT_FOUNDATION §15, §26, §31 (Presentation).
 * V1_SCREEN_SPECIFICATION §63. V1_USER_EXPERIENCE §45.
 * V1_TECHNICAL_ARCHITECTURE §57.
 *
 * §15 requires user-facing language, not technical exceptions. The
 * message parameter is the plain-language explanation. Optional
 * details are progressively disclosed (§2.2) via a toggle.
 *
 * Ships uncalled (A194=a). First consumer arrives with the first
 * screen that handles a DomainError.
 *
 * The error icon uses the Clay semantic colour via the theme's error
 * role. §59 requires more than colour: the icon and the message text
 * also communicate the state.
 */
@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    details: String? = null,
) {
    var detailsVisible by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(Spacing.screenMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp),
        )

        Spacer(Modifier.height(Spacing.s))

        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        if (onRetry != null) {
            Spacer(Modifier.height(Spacing.xl))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.error_retry))
            }
        }

        if (details != null) {
            Spacer(Modifier.height(Spacing.m))
            TextButton(onClick = { detailsVisible = !detailsVisible }) {
                Text(
                    stringResource(
                        if (detailsVisible) {
                            R.string.error_details_hide
                        } else {
                            R.string.error_details_show
                        },
                    ),
                )
            }
            if (detailsVisible) {
                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
