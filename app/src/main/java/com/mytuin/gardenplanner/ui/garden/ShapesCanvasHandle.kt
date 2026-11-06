package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

/**
 * Shared handle for the MapView used by GardenShapesCanvas (Step 9i).
 *
 * Owns the MapView lifecycle (remembered, started/stopped via
 * LifecycleObserver, destroyed in onDispose) plus the mutable refs
 * that shape effects read. Callers that need extra layers or
 * gestures (the editor) get access to the style and map via this
 * handle's public properties; callers that only need display (the
 * history screen) ignore them.
 */
class ShapesCanvasHandle internal constructor(
    internal val mapView: MapView,
    internal val mapRef: MutableState<MapLibreMap?>,
    internal val styleRef: MutableState<Style?>,
    internal val styleVersion: MutableIntState,
    internal val recenterTrigger: MutableIntState,
) {
    val currentStyle: Style? get() = styleRef.value
    val currentStyleVersion: Int get() = styleVersion.intValue
    val currentMap: MapLibreMap? get() = mapRef.value

    fun recenter() {
        recenterTrigger.intValue += 1
    }
}
