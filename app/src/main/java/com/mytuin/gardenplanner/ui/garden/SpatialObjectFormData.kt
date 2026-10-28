package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * Validated output of [SpatialObjectFormState] (Step 9e).
 *
 * Two shapes because CREATE requires geometry (A5) and EDIT does
 * not touch it (Step 9e scope). The screen chooses which to build
 * based on mode; the ViewModel receives exactly one shape.
 */
sealed interface SpatialObjectFormData {
    data class ForCreate(
        val objectType: InfrastructureType,
        val geometry: Geometry,
        val name: String?,
        val description: String?,
        val areaId: String?,
        val notes: String?,
    ) : SpatialObjectFormData

    data class ForEdit(
        val fields: SpatialObjectFields,
    ) : SpatialObjectFormData
}
