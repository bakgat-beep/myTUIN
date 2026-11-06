package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.theme.Spacing

private val SLIDER_SURFACE_ELEVATION = 2.dp

/**
 * Time scrub slider for the history screen (Step 9i).
 *
 * Normalises the [minTime, maxTime] range to [0f, 1f] for the
 * Slider. The Float precision (~24 bits) means multi-year ranges
 * step in minutes, single-day ranges step in seconds — both fine
 * for scrubbing. The "Jump to now" button is disabled when the
 * slider is already at maxTime.
 */
@Composable
fun HistorySlider(
    currentTime: Long,
    minTime: Long,
    maxTime: Long,
    onTimeChange: (Long) -> Unit,
    onJumpToNow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val range = (maxTime - minTime).coerceAtLeast(1L)
    val fraction = ((currentTime - minTime).toDouble() / range.toDouble()).coerceIn(0.0, 1.0).toFloat()
    val label = HistoryDateFormat.format(currentTime, minTime, maxTime)

    Surface(
        modifier = modifier,
        tonalElevation = SLIDER_SURFACE_ELEVATION,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenMargin, vertical = Spacing.s),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
            )
            Slider(
                value = fraction,
                onValueChange = { f ->
                    val t = minTime + (f.toDouble() * range).toLong()
                    onTimeChange(t)
                },
                valueRange = 0f..1f,
            )
            TextButton(
                onClick = onJumpToNow,
                enabled = currentTime < maxTime,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(stringResource(R.string.history_slider_jump_to_now))
            }
        }
    }
}
