package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.R

private val BADGE_SIZE = 10.dp
private val BADGE_OFFSET = 2.dp

/**
 * Entry point to the layers/filters sheet (Step 9h).
 *
 * The badge is a small filled dot in the top-end corner when at
 * least one filter is non-default. It is deliberately a dot and not
 * a count: the count would have to mean "hidden kinds? hidden
 * types? hidden shapes?" and none of those is a satisfying answer.
 * A dot says "you have non-default filters on" which is enough to
 * motivate a tap.
 */
@Composable
fun MapLayersFab(
    showBadge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        FloatingActionButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.Layers,
                contentDescription = stringResource(R.string.map_layers_fab_content_description),
            )
        }
        if (showBadge) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = BADGE_OFFSET, y = -BADGE_OFFSET)
                        .size(BADGE_SIZE)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error),
            )
        }
    }
}
