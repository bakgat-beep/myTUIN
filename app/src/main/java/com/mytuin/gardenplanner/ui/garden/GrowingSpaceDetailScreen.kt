package com.mytuin.gardenplanner.ui.garden

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.ui.components.ErrorState
import com.mytuin.gardenplanner.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowingSpaceDetailScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GrowingSpaceDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            uiState.space?.name
                                ?: stringResource(R.string.nav_garden),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_action),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        GrowingSpaceDetailContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
fun GrowingSpaceDetailContent(
    uiState: GrowingSpaceDetailUiState,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading ->
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        uiState.loadError || uiState.space == null ->
            ErrorState(
                message = stringResource(R.string.growing_space_detail_load_error),
                modifier = modifier,
            )
        else ->
            LoadedGrowingSpaceDetail(space = uiState.space, modifier = modifier)
    }
}

@Composable
private fun LoadedGrowingSpaceDetail(
    space: GrowingSpace,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Spacing.screenMargin),
        verticalArrangement = Arrangement.spacedBy(Spacing.m),
    ) {
        Text(
            text = typeAndStatusLine(space),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        DescriptionSection(space)
        DimensionsSection(space)
        GeometrySection(space)
        NotesSection(space)
    }
}

@Composable
private fun typeAndStatusLine(space: GrowingSpace): String =
    stringResource(
        R.string.growing_space_detail_type_and_status_format,
        stringResource(spaceTypeLabelRes(space.spaceType)),
        stringResource(statusLabelRes(space.status)),
    )

@Composable
private fun DescriptionSection(space: GrowingSpace) {
    val description = space.description
    if (description.isNullOrBlank()) return
    Text(
        text = stringResource(R.string.growing_space_detail_section_description),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun DimensionsSection(space: GrowingSpace) {
    if (!hasAnyDimension(space)) return
    Text(
        text = stringResource(R.string.growing_space_detail_section_dimensions),
        style = MaterialTheme.typography.titleMedium,
    )
    space.lengthMetres?.let {
        Text(stringResource(R.string.growing_space_detail_length_format, formatDimension(it)))
    }
    space.widthMetres?.let {
        Text(stringResource(R.string.growing_space_detail_width_format, formatDimension(it)))
    }
    space.heightMetres?.let {
        Text(stringResource(R.string.growing_space_detail_height_format, formatDimension(it)))
    }
    space.diameterMetres?.let {
        Text(stringResource(R.string.growing_space_detail_diameter_format, formatDimension(it)))
    }
    space.areaSquareMetres?.let {
        Text(stringResource(R.string.growing_space_detail_area_format, formatDimension(it)))
    }
    space.volumeCubicMetres?.let {
        Text(stringResource(R.string.growing_space_detail_volume_format, formatDimension(it)))
    }
}

@Composable
private fun GeometrySection(space: GrowingSpace) {
    Text(
        text = stringResource(R.string.growing_space_detail_section_geometry),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = stringResource(geometryLabelRes(space.geometry?.geometryType)),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun NotesSection(space: GrowingSpace) {
    val notes = space.notes
    if (notes.isNullOrBlank()) return
    Text(
        text = stringResource(R.string.growing_space_detail_section_notes),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = notes,
        style = MaterialTheme.typography.bodyMedium,
    )
}

private fun hasAnyDimension(space: GrowingSpace): Boolean =
    space.lengthMetres != null ||
        space.widthMetres != null ||
        space.heightMetres != null ||
        space.diameterMetres != null ||
        space.areaSquareMetres != null ||
        space.volumeCubicMetres != null

@StringRes
private fun geometryLabelRes(type: GeometryType?): Int =
    when (type) {
        GeometryType.POINT -> R.string.growing_space_detail_geometry_point
        GeometryType.LINE -> R.string.growing_space_detail_geometry_line
        GeometryType.POLYGON -> R.string.growing_space_detail_geometry_polygon
        null -> R.string.growing_space_detail_geometry_not_set
    }
