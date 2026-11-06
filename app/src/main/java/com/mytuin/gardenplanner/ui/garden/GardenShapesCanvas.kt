package com.mytuin.gardenplanner.ui.garden

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
 * Shared map canvas (Step 9i) used by both the interactive Garden
 * tab and the read-only history screen.
 *
 * Renders base shape layers (areas, spatial objects, growing spaces),
 * fits the camera to [viewport], and offers a recentre FAB. Anything
 * extra — selection overlays, gesture handlers, edit controls — is
 * added by the caller via [onMapReady] (layers + handlers) and the
 * [overlay] slot (composables). The editor uses both; history uses
 * neither.
 */
@Composable
fun rememberShapesCanvasHandle(): ShapesCanvasHandle {
    val context = LocalContext.current
    val handle =
        remember(context) {
            ShapesCanvasHandle(
                mapView = MapView(context).also { it.onCreate(null) },
                mapRef = mutableStateOf(null),
                styleRef = mutableStateOf(null),
                styleVersion = mutableIntStateOf(0),
                recenterTrigger = mutableIntStateOf(0),
            )
        }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(handle.mapView, lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> handle.mapView.onStart()
                    Lifecycle.Event.ON_RESUME -> handle.mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> handle.mapView.onPause()
                    Lifecycle.Event.ON_STOP -> handle.mapView.onStop()
                    else -> Unit
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            handle.mapView.onDestroy()
        }
    }
    return handle
}

@Composable
fun GardenShapesCanvas(
    handle: ShapesCanvasHandle,
    shapes: List<MapShape>,
    viewport: MapViewport,
    origin: Wgs84,
    modifier: Modifier = Modifier,
    onMapReady: ((MapLibreMap, MapView, Style) -> Unit)? = null,
    overlay: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                handle.mapView.apply {
                    getMapAsync { map ->
                        handle.mapRef.value = map
                        map.setStyle(MapLibreStyleConstants.STYLE_URL) { style ->
                            handle.styleRef.value = style
                            GardenMapStyle.install(style)
                            handle.styleVersion.intValue += 1
                            onMapReady?.invoke(map, handle.mapView, style)
                        }
                    }
                }
            },
        )
        BaseShapeLayersEffect(
            styleRef = handle.styleRef.value,
            styleVersion = handle.styleVersion.intValue,
            shapes = shapes,
            origin = origin,
        )
        FitViewportEffect(
            mapRef = handle.mapRef.value,
            styleVersion = handle.styleVersion.intValue,
            viewport = viewport,
            origin = origin,
            recenterTrigger = handle.recenterTrigger.intValue,
        )
        RecenterFab(onClick = handle::recenter)
        overlay()
    }
}

@Composable
private fun BaseShapeLayersEffect(
    styleRef: Style?,
    styleVersion: Int,
    shapes: List<MapShape>,
    origin: Wgs84,
) {
    LaunchedEffect(styleVersion, shapes) {
        if (styleVersion == 0) return@LaunchedEffect
        val style = styleRef ?: return@LaunchedEffect
        style
            .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_AREAS)
            ?.setGeoJson(GardenMapFeatures.areas(shapes, origin))
        style
            .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_SPATIAL_OBJECTS)
            ?.setGeoJson(GardenMapFeatures.spatialObjects(shapes, origin))
        style
            .getSourceAs<GeoJsonSource>(MapLibreStyleConstants.SOURCE_GROWING_SPACES)
            ?.setGeoJson(GardenMapFeatures.growingSpaces(shapes, origin))
    }
}

@Composable
private fun FitViewportEffect(
    mapRef: MapLibreMap?,
    styleVersion: Int,
    viewport: MapViewport,
    origin: Wgs84,
    recenterTrigger: Int,
) {
    LaunchedEffect(styleVersion, viewport, recenterTrigger) {
        if (styleVersion == 0) return@LaunchedEffect
        val map = mapRef ?: return@LaunchedEffect
        fitViewport(map, viewport, origin)
    }
}

@Composable
private fun BoxScope.RecenterFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
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
