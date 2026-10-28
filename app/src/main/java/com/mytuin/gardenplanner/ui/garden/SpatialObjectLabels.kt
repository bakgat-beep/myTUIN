package com.mytuin.gardenplanner.ui.garden

import androidx.annotation.StringRes
import com.mytuin.gardenplanner.R
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * String resources shared between the spatial object list, detail
 * and form screens (Step 9e).
 *
 * statusLabelRes and the geometry labels are reused from
 * GrowingSpaceLabels.kt — RecordStatus and GeometryType are shared
 * vocabularies, and duplicating the mapping would risk drift.
 */

@StringRes
internal fun infrastructureTypeLabelRes(type: InfrastructureType): Int =
    when (type) {
        InfrastructureType.PATH -> R.string.spatial_object_type_path
        InfrastructureType.FENCE -> R.string.spatial_object_type_fence
        InfrastructureType.WALL -> R.string.spatial_object_type_wall
        InfrastructureType.GATE -> R.string.spatial_object_type_gate
        InfrastructureType.BUILDING -> R.string.spatial_object_type_building
        InfrastructureType.SHED -> R.string.spatial_object_type_shed
        InfrastructureType.GREENHOUSE_STRUCTURE -> R.string.spatial_object_type_greenhouse_structure
        InfrastructureType.IRRIGATION -> R.string.spatial_object_type_irrigation
        InfrastructureType.WATER_SOURCE -> R.string.spatial_object_type_water_source
        InfrastructureType.DRAIN -> R.string.spatial_object_type_drain
        InfrastructureType.COMPOST_AREA -> R.string.spatial_object_type_compost_area
        InfrastructureType.STORAGE_AREA -> R.string.spatial_object_type_storage_area
        InfrastructureType.SUPPORT_STRUCTURE -> R.string.spatial_object_type_support_structure
        InfrastructureType.TRELLIS -> R.string.spatial_object_type_trellis
        InfrastructureType.SHADE_STRUCTURE -> R.string.spatial_object_type_shade_structure
        InfrastructureType.LIGHTING -> R.string.spatial_object_type_lighting
        InfrastructureType.UTILITY -> R.string.spatial_object_type_utility
        InfrastructureType.OTHER -> R.string.spatial_object_type_other
    }

/**
 * Display name for an object: its name if present, otherwise the
 * supplied type label. The label is passed in because callers
 * already resolve it (they need it for the secondary line when
 * name is present too), so taking InfrastructureType as well would
 * be redundant.
 */
internal fun spatialObjectDisplayName(
    name: String?,
    typeLabel: String,
): String = name?.takeIf { it.isNotBlank() } ?: typeLabel
