package com.mytuin.gardenplanner.ui.garden.debug

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.ui.theme.Spacing
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.geojson.Polygon

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PocEntryPoint {
    fun gardenRepository(): GardenRepository

    fun growingSpaceRepository(): GrowingSpaceRepository
}

private const val STYLE_URL = "https://demotiles.maplibre.org/style.json"

private const val SOURCE_BOUNDARY = "poc-boundary"
private const val SOURCE_SPACES = "poc-spaces"
private const val SOURCE_SELECTED = "poc-selected"
private const val SOURCE_VERTICES = "poc-vertices"

private const val LAYER_SPACES_FILL = "poc-spaces-fill"
private const val LAYER_SPACES_LINE = "poc-spaces-line"
private const val LAYER_BOUNDARY_FILL = "poc-boundary-fill"
private const val LAYER_BOUNDARY_LINE = "poc-boundary-line"
private const val LAYER_SELECTED_FILL = "poc-selected-fill"
private const val LAYER_SELECTED_LINE = "poc-selected-line"
private const val LAYER_VERTICES = "poc-vertices-circle"

private const val FALLBACK_LATITUDE = -43.5321
private const val FALLBACK_LONGITUDE = 172.6362

private const val FALLBACK_BOUNDARY_METRES = 10.0

@Composable
fun MapLibrePoc(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val garden: Garden?
    val spaces: List<GrowingSpace>
    runBlocking {
        val entryPoint =
            EntryPointAccessors.fromApplication(
                context.applicationContext,
                PocEntryPoint::class.java,
            )
        garden =
            entryPoint
                .gardenRepository()
                .observeGardens()
                .first()
                .firstOrNull()
        spaces = garden?.let {
            entryPoint
                .growingSpaceRepository()
                .observeGrowingSpacesInGarden(it.id)
                .first()
        } ?: emptyList()
    }

    if (garden == null) {
        PocEmptyState(modifier)
        return
    }

    val origin: Wgs84 =
        Wgs84(
            latitude = garden.latitude ?: FALLBACK_LATITUDE,
            longitude = garden.longitude ?: FALLBACK_LONGITUDE,
        )

    var mapRef by remember { mutableStateOf<MapLibreMap?>(null) }
    var selectedSpaceId by remember { mutableStateOf<String?>(null) }
    var editedGeometry by remember { mutableStateOf<Geometry.Polygon?>(null) }
    var styleVersion by remember { mutableStateOf(0) }

    val initialBoundary = remember(spaces) { computeBoundary(spaces) }
    var boundary by remember(spaces) { mutableStateOf(initialBoundary) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                MapView(ctx).apply {
                    onCreate(null)
                    getMapAsync { map ->
                        mapRef = map
                        map.setStyle(STYLE_URL) { style ->
                            style.addSource(
                                GeoJsonSource(SOURCE_BOUNDARY, boundaryFeature(boundary, origin)),
                            )
                            style.addSource(
                                GeoJsonSource(SOURCE_SPACES, spacesFeatureCollection(spaces, origin)),
                            )
                            style.addSource(
                                GeoJsonSource(SOURCE_SELECTED, emptyCollection()),
                            )
                            style.addSource(
                                GeoJsonSource(SOURCE_VERTICES, emptyCollection()),
                            )

                            style.addLayer(
                                FillLayer(LAYER_BOUNDARY_FILL, SOURCE_BOUNDARY)
                                    .withProperties(
                                        PropertyFactory.fillColor("#F2F0E9"),
                                        PropertyFactory.fillOpacity(0.4f),
                                        PropertyFactory.fillOutlineColor("#DDD9CE"),
                                    ),
                            )
                            style.addLayer(
                                LineLayer(LAYER_BOUNDARY_LINE, SOURCE_BOUNDARY)
                                    .withProperties(
                                        PropertyFactory.lineColor("#DDD9CE"),
                                        PropertyFactory.lineWidth(2f),
                                    ),
                            )
                            style.addLayer(
                                FillLayer(LAYER_SPACES_FILL, SOURCE_SPACES)
                                    .withProperties(
                                        PropertyFactory.fillColor("#7D9A82"),
                                        PropertyFactory.fillOpacity(0.5f),
                                        PropertyFactory.fillOutlineColor("#356859"),
                                    ),
                            )
                            style.addLayer(
                                LineLayer(LAYER_SPACES_LINE, SOURCE_SPACES)
                                    .withProperties(
                                        PropertyFactory.lineColor("#356859"),
                                        PropertyFactory.lineWidth(2.5f),
                                    ),
                            )
                            style.addLayer(
                                FillLayer(LAYER_SELECTED_FILL, SOURCE_SELECTED)
                                    .withProperties(
                                        PropertyFactory.fillColor("#B4863A"),
                                        PropertyFactory.fillOpacity(0.35f),
                                    ),
                            )
                            style.addLayer(
                                LineLayer(LAYER_SELECTED_LINE, SOURCE_SELECTED)
                                    .withProperties(
                                        PropertyFactory.lineColor("#B4863A"),
                                        PropertyFactory.lineWidth(3f),
                                    ),
                            )
                            style.addLayer(
                                CircleLayer(LAYER_VERTICES, SOURCE_VERTICES)
                                    .withProperties(
                                        PropertyFactory.circleColor("#23483E"),
                                        PropertyFactory.circleRadius(7f),
                                        PropertyFactory.circleStrokeColor("#FAF9F5"),
                                        PropertyFactory.circleStrokeWidth(2f),
                                    ),
                            )
                        }

                        map.cameraPosition =
                            CameraPosition
                                .Builder()
                                .target(LatLng(origin.latitude, origin.longitude))
                                .zoom(18.5)
                                .build()

                        map.addOnMapClickListener { latLng ->
                            handleMapClick(
                                latLng = latLng,
                                map = map,
                                origin = origin,
                                selectedSpaceId = selectedSpaceId,
                                editedGeometry = editedGeometry,
                                onSelect = { id ->
                                    selectedSpaceId = id
                                    editedGeometry = null
                                    styleVersion += 1
                                },
                                onClearSelection = {
                                    selectedSpaceId = null
                                    editedGeometry = null
                                    styleVersion += 1
                                },
                                onAddVertex = { coordinate ->
                                    val current = editedGeometry
                                    if (current != null) {
                                        editedGeometry = appendVertex(current, coordinate)
                                        styleVersion += 1
                                    }
                                },
                            )
                        }

                        map.addOnMapLongClickListener { latLng ->
                            if (editedGeometry != null) {
                                val coordinate =
                                    MetresToWgs84.toCoordinate(
                                        origin,
                                        Wgs84(latLng.latitude, latLng.longitude),
                                    )
                                editedGeometry = appendVertex(editedGeometry!!, coordinate)
                                styleVersion += 1
                                true
                            } else {
                                false
                            }
                        }
                    }
                }
            },
            update = { /* state-driven rendering happens via LaunchedEffect below */ },
        )

        LaunchedEffect(styleVersion, selectedSpaceId, editedGeometry, boundary) {
            val map = mapRef ?: return@LaunchedEffect
            map.getStyle { style ->
                updateSources(
                    style = style,
                    boundary = boundary,
                    spaces = spaces,
                    selectedSpaceId = selectedSpaceId,
                    editedGeometry = editedGeometry,
                    origin = origin,
                )
            }
        }

        PocControls(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(Spacing.l),
            selectedName = spaces.firstOrNull { it.id == selectedSpaceId }?.name,
            isEditing = editedGeometry != null,
            onStartEditing = {
                val selected = spaces.firstOrNull { it.id == selectedSpaceId }
                val polygon = selected?.geometry as? Geometry.Polygon
                if (polygon != null) {
                    editedGeometry = polygon
                    styleVersion += 1
                }
            },
            onFinishEditing = {
                editedGeometry = null
                styleVersion += 1
            },
            onReset = {
                editedGeometry = null
                selectedSpaceId = null
                styleVersion += 1
            },
        )
    }
}

@Composable
private fun PocEmptyState(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.poc_map_empty_state),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun PocControls(
    selectedName: String?,
    isEditing: Boolean,
    onStartEditing: () -> Unit,
    onFinishEditing: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = selectedName ?: stringResource(R.string.poc_map_no_selection),
            style = MaterialTheme.typography.labelLarge,
        )
        Row(modifier = Modifier.padding(top = Spacing.s)) {
            if (isEditing) {
                Button(onClick = onFinishEditing) {
                    Text(stringResource(R.string.poc_map_finish_editing))
                }
            } else {
                Button(
                    onClick = onStartEditing,
                    enabled = selectedName != null,
                ) {
                    Text(stringResource(R.string.poc_map_edit))
                }
            }
            Button(
                onClick = onReset,
                modifier = Modifier.padding(start = Spacing.s),
            ) {
                Text(stringResource(R.string.poc_map_reset))
            }
        }
    }
}

private fun computeBoundary(spaces: List<GrowingSpace>): List<Coordinate> {
    val polygons = spaces.mapNotNull { it.geometry as? Geometry.Polygon }
    if (polygons.isEmpty()) {
        return listOf(
            Coordinate(-FALLBACK_BOUNDARY_METRES, -FALLBACK_BOUNDARY_METRES),
            Coordinate(FALLBACK_BOUNDARY_METRES, -FALLBACK_BOUNDARY_METRES),
            Coordinate(FALLBACK_BOUNDARY_METRES, FALLBACK_BOUNDARY_METRES),
            Coordinate(-FALLBACK_BOUNDARY_METRES, FALLBACK_BOUNDARY_METRES),
            Coordinate(-FALLBACK_BOUNDARY_METRES, -FALLBACK_BOUNDARY_METRES),
        )
    }
    val xs = polygons.flatMap { polygon -> polygon.ring.map { it.x } }
    val ys = polygons.flatMap { polygon -> polygon.ring.map { it.y } }
    val minX = xs.min() - 2.0
    val maxX = xs.max() + 2.0
    val minY = ys.min() - 2.0
    val maxY = ys.max() + 2.0
    return listOf(
        Coordinate(minX, minY),
        Coordinate(maxX, minY),
        Coordinate(maxX, maxY),
        Coordinate(minX, maxY),
        Coordinate(minX, minY),
    )
}

private fun boundaryFeature(
    boundary: List<Coordinate>,
    origin: Wgs84,
): Feature = Feature.fromGeometry(polygonGeometry(boundary, origin))

private fun spacesFeatureCollection(
    spaces: List<GrowingSpace>,
    origin: Wgs84,
): FeatureCollection {
    val features =
        spaces.mapNotNull { space ->
            val polygon = space.geometry as? Geometry.Polygon ?: return@mapNotNull null
            Feature
                .fromGeometry(polygonGeometry(polygon.ring, origin))
                .apply { addStringProperty("spaceId", space.id) }
        }
    return FeatureCollection.fromFeatures(features)
}

private fun polygonGeometry(
    ring: List<Coordinate>,
    origin: Wgs84,
): Polygon {
    val points =
        ring.map { coordinate ->
            val wgs = MetresToWgs84.toWgs84(origin, coordinate)
            Point.fromLngLat(wgs.longitude, wgs.latitude)
        }
    return Polygon.fromLngLats(listOf(points))
}

private fun emptyCollection(): FeatureCollection = FeatureCollection.fromFeatures(emptyList<Feature>())

private fun updateSources(
    style: Style,
    boundary: List<Coordinate>,
    spaces: List<GrowingSpace>,
    selectedSpaceId: String?,
    editedGeometry: Geometry.Polygon?,
    origin: Wgs84,
) {
    (style.getSourceAs<GeoJsonSource>(SOURCE_BOUNDARY))
        ?.setGeoJson(boundaryFeature(boundary, origin))

    (style.getSourceAs<GeoJsonSource>(SOURCE_SPACES))
        ?.setGeoJson(spacesFeatureCollection(spaces, origin))

    val selected =
        if (editedGeometry != null) {
            editedGeometry
        } else {
            (spaces.firstOrNull { it.id == selectedSpaceId }?.geometry as? Geometry.Polygon)
        }

    if (selected != null) {
        val feature = Feature.fromGeometry(polygonGeometry(selected.ring, origin))
        style
            .getSourceAs<GeoJsonSource>(SOURCE_SELECTED)
            ?.setGeoJson(FeatureCollection.fromFeatures(listOf(feature)))
        style
            .getSourceAs<GeoJsonSource>(SOURCE_VERTICES)
            ?.setGeoJson(
                FeatureCollection.fromFeatures(
                    selected.ring.map { coordinate ->
                        val wgs = MetresToWgs84.toWgs84(origin, coordinate)
                        Feature.fromGeometry(
                            Point.fromLngLat(wgs.longitude, wgs.latitude),
                        )
                    },
                ),
            )
    } else {
        style.getSourceAs<GeoJsonSource>(SOURCE_SELECTED)?.setGeoJson(emptyCollection())
        style.getSourceAs<GeoJsonSource>(SOURCE_VERTICES)?.setGeoJson(emptyCollection())
    }
}

private fun handleMapClick(
    latLng: LatLng,
    map: MapLibreMap,
    origin: Wgs84,
    selectedSpaceId: String?,
    editedGeometry: Geometry.Polygon?,
    onSelect: (String) -> Unit,
    onClearSelection: () -> Unit,
    onAddVertex: (Coordinate) -> Unit,
): Boolean {
    val pixel = map.projection.toScreenLocation(latLng)
    val hits = map.queryRenderedFeatures(pixel, LAYER_SPACES_FILL)
    if (hits.isNotEmpty()) {
        val id = hits.first().getStringProperty("spaceId")
        if (id != null) {
            onSelect(id)
            return true
        }
    }

    if (editedGeometry != null) {
        val coordinate =
            MetresToWgs84.toCoordinate(
                origin,
                Wgs84(latLng.latitude, latLng.longitude),
            )
        onAddVertex(coordinate)
        return true
    }

    if (selectedSpaceId != null) {
        onClearSelection()
        return true
    }
    return false
}

private fun appendVertex(
    polygon: Geometry.Polygon,
    coordinate: Coordinate,
): Geometry.Polygon {
    val ring = polygon.ring.toMutableList()
    ring.add(ring.lastIndex, coordinate)
    return Geometry.Polygon(ring)
}
