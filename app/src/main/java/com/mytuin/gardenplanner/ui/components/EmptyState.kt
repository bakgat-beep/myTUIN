package com.mytuin.gardenplanner.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.ui.theme.Spacing

/**
 * A useful empty state for a screen with nothing to show.
 *
 * PHASE_0_PROJECT_FOUNDATION §26, §31 (Presentation). V1_SCREEN_SPECIFICATION
 * §62. V1_USER_EXPERIENCE §44. V1_VISUAL_DESIGN_SPECIFICATION §47.
 *
 * §47 requires: what is missing, why the area matters, the most useful
 * next action. This composable covers all three.
 *
 * The optional action parameter (A193=a) exists because real screens
 * will need it. Placeholder screens do not pass one, because there is
 * nothing the button can honestly do yet.
 *
 * The optional icon (A200=a) is small per §47 ("avoid large
 * illustrations"). Null by default; callers opt in.
 *
 * All dimensions come from the design tokens (PHASE_0_PROJECT_FOUNDATION
 * §30 item 20; V1_VISUAL_DESIGN_SPECIFICATION §63). No literal values.
 */
@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(Spacing.screenMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp),
            )
            Spacer(Modifier.height(Spacing.s))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Spacing.s))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(Spacing.xl))
            Button(onClick = onAction) {
                Text(actionLabel)
            }
        }
    }
}
