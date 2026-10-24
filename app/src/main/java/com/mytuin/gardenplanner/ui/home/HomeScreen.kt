package com.mytuin.gardenplanner.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.components.EmptyState
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

/**
 * Wired Home screen. Reads state from HomeViewModel and delegates
 * rendering to HomeContent.
 *
 * D9a-2: the split between HomeScreen (wired) and HomeContent
 * (stateless) means Compose tests target HomeContent directly, with
 * no ViewModel dependency.
 */
@Composable
fun HomeScreen(
    onNavigateToCreateGarden: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onNavigateToCreateGarden = onNavigateToCreateGarden,
        modifier = modifier,
    )
}

/**
 * Stateless Home screen.
 */
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onNavigateToCreateGarden: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        HomeUiState.Loading ->
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        HomeUiState.NoGarden ->
            EmptyState(
                title = stringResource(R.string.home_no_garden_title),
                description = stringResource(R.string.home_no_garden_description),
                icon = Icons.Filled.Yard,
                actionLabel = stringResource(R.string.home_no_garden_action),
                onAction = onNavigateToCreateGarden,
                modifier = modifier,
            )
        is HomeUiState.Ready ->
            GardenSummary(garden = uiState.garden, modifier = modifier)
        HomeUiState.Error ->
            ErrorState(
                message = stringResource(R.string.home_error_generic),
                modifier = modifier,
            )
    }
}

@Composable
private fun GardenSummary(
    garden: Garden,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        Text(
            text = garden.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(statusLabelRes(garden.status)),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (!garden.description.isNullOrBlank()) {
            Text(
                text = garden.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@StringRes
private fun statusLabelRes(status: RecordStatus): Int =
    when (status) {
        RecordStatus.DRAFT -> R.string.home_garden_summary_status_draft
        RecordStatus.ACTIVE -> R.string.home_garden_summary_status_active
        RecordStatus.INACTIVE -> R.string.home_garden_summary_status_inactive
        RecordStatus.DEPRECATED -> R.string.home_garden_summary_status_deprecated
        RecordStatus.ARCHIVED -> R.string.home_garden_summary_status_archived
    }
