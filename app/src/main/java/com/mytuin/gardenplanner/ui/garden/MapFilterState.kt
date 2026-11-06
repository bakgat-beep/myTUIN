package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * View-only filter state for the Garden map (Step 9h).
 *
 * Nothing here is persisted or observed by the repositories. It is a
 * presentation concern: it decides which of the shapes that already
 * exist are drawn, and nothing else. Layer toggles and type filters
 * do not change any entity, do not write history, and do not affect
 * what the app can do — they only affect what the user sees.
 *
 * Three booleans toggle whole kinds. Two sets carry per-subtype
 * exclusions within a kind. The sets are named "hidden" because the
 * UI's "checked = visible, unchecked = hidden" chip semantics maps
 * to `type !in hidden` — the field records what is excluded, the UI
 * shows what is included. This is the natural reading when a new
 * vocabulary value is added later: it defaults to visible rather
 * than silently hidden.
 *
 * isShapeVisible is exhaustive over MapShapeKind. Adding a fourth
 * kind to the sealed interface becomes a compile error here, which
 * is the correct response — the new kind needs an explicit
 * decision about whether it is filterable.
 */
data class MapFilterState(
    val showAreas: Boolean = true,
    val showGrowingSpaces: Boolean = true,
    val showSpatialObjects: Boolean = true,
    val hiddenGrowingSpaceTypes: Set<GrowingSpaceType> = emptySet(),
    val hiddenInfrastructureTypes: Set<InfrastructureType> = emptySet(),
) {
    val isDefault: Boolean
        get() =
            showAreas &&
                showGrowingSpaces &&
                showSpatialObjects &&
                hiddenGrowingSpaceTypes.isEmpty() &&
                hiddenInfrastructureTypes.isEmpty()

    val visibleKindCount: Int
        get() =
            (if (showAreas) 1 else 0) +
                (if (showGrowingSpaces) 1 else 0) +
                (if (showSpatialObjects) 1 else 0)

    fun isShapeVisible(shape: MapShape): Boolean =
        when (val kind = shape.kind) {
            is MapShapeKind.Area -> showAreas
            is MapShapeKind.GrowingSpace -> showGrowingSpaces && kind.type !in hiddenGrowingSpaceTypes
            is MapShapeKind.SpatialObject -> showSpatialObjects && kind.type !in hiddenInfrastructureTypes
        }
}
