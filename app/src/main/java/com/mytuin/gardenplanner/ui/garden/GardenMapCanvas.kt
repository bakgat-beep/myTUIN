package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.ui.theme.Spacing
import kotlin.math.log2
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.sources.GeoJsonSource

/**
 * Map surface for Step 9f. Read-only in 9f: pan, zoom, re-centre.
 *
 * Reads MapShape values from the ViewModel and pushes them into
 * MapLibre via GeoJSON sources on every emission. No tap handlers,
 * no editing, no selection state.
 *
 * Lifecycle: the MapView is remembered for the composition's
 * lifetime and destroyed in onDispose. onStart/onResume/onPause/
 * onStop are not driven because MapLibre's Compose integration in
 * 9f deliberately keeps a simple shape — the MapView is created
 * once and reused.
 */
@Composable
fun GardenMapCanvas(
    uiState: GardenMapUiState.Ready,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context).also { it.onCreate(null) } }
    var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }
    var styleRef by remember { mutableStateOf<Style?>(null) }
    var styleVersion by remember { mutableIntStateOf(0) }
    var recenterTrigger by remember { mutableIntStateOf(0) }

    DisposableEffect(mapView) {
        onDispose { mapView.onDestroy() }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                mapView.apply {
                    getMapAsync { map ->
                        mapRef = map
                        map.setStyle(MapLibreStyleConstants.STYLE_URL) { style ->
                            styleRef = style
                            GardenMapStyle.install(style)
                            styleVersion += 1
                        }
                    }
                }
            },
        )

        LaunchedEffect(styleVersion, uiState) {
            if (styleVersion == 0) return@LaunchedEffect
            val style = styleRef ?: return@LaunchedEffect
            val origin = uiState.origin
            style
                .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_AREAS)
                ?.setGeoJson(GardenMapFeatures.areas(uiState.shapes, origin))
            style
                .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS)
                ?.setGeoJson(GardenMapFeatures.spatialObjects(uiState.shapes, origin))
            style
                .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_GROWING_SPACES)
                ?.setGeoJson(GardenMapFeatures.growingSpaces(uiState.shapes, origin))
        }

        LaunchedEffect(styleVersion, uiState.viewport, recenterTrigger) {
            if (styleVersion == 0) return@LaunchedEffect
            val map = mapRef ?: return@LaunchedEffect
            fitViewport(map, uiState.viewport, uiState.origin)
        }

        FloatingActionButton(
            onClick = { recenterTrigger += 1 },
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Spacing.l),
        ) {
            Icon(
                imageVector = Icons.Filled.MyLocation,
                contentDescription = stringResource(R.string.garden_map_recentre_action),
            )
        }
    }
}

private const val FALLBACK_ZOOM = 17.0
private const val ZOOM_SCALE_BASE = 17.0
private const val MIN_ZOOM = 12.0
private const val MAX_ZOOM = 20.0

private fun fitViewport(
    map: MapLibreMap,
    viewport: MapViewport,
    origin: Wgs84,
) {
    val centreWgs =
        MetresToWgs84.toWgs84(
            origin,
            Coordinate(viewport.centerX, viewport.centerY),
        )
    val largestDimension = viewport.largestDimensionMetres
    val zoom =
        if (largestDimension <= 0.0) {
            FALLBACK_ZOOM
        } else {
            (ZOOM_SCALE_BASE - log2(largestDimension)).coerceIn(MIN_ZOOM, MAX_ZOOM)
        }
    map.cameraPosition =
        CameraPosition
            .Builder()
            .target(LatLng(centreWgs.latitude, centreWgs.longitude))
            .zoom(zoom)
            .build()
}
