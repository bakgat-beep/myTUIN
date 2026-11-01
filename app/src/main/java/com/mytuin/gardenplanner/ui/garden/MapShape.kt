package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * One drawable shape on the garden map (Step 9f).
 *
 * Bundles the domain geometry with enough information for the map
 * to style it: the semantic kind (growing space / spatial object /
 * area) plus the specific subtype used by MapShapeStyle.
 *
 * The map layer never sees domain entities directly — it consumes
 * MapShape values built in GardenMapViewModel. This keeps the
 * AndroidView / MapLibre surface independent of the domain model.
 */
data class MapShape(
    val id: String,
    val kind: MapShapeKind,
    val geometry: Geometry,
)

sealed interface MapShapeKind {
    data class GrowingSpace(
        val type: GrowingSpaceType,
    ) : MapShapeKind

    data class SpatialObject(
        val type: InfrastructureType,
    ) : MapShapeKind

    data object Area : MapShapeKind
}
