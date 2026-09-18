package com.mytuin.gardenplanner.ui.inbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.ui.components.EmptyState

@Composable
fun InboxScreen(modifier: Modifier = Modifier) {
    EmptyState(
        title = stringResource(R.string.empty_inbox_title),
        description = stringResource(R.string.empty_inbox_description),
        icon = Icons.Filled.Inbox,
        modifier = modifier.fillMaxSize(),
    )
}