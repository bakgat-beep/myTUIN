package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * Initial values for the spatial object form (Step 9e).
 *
 * Mirrors GrowingSpaceFormSeed: one seed argument rather than ten
 * parameters, raw-text geometry fields so recomposition does not
 * re-derive display strings from Doubles.
 *
 * For EDIT mode, rect fields stay blank because geometry is not
 * editable in 9e (Step 9e: scalar-only edit). The existing geometry
 * is shown as a read-only label in the form.
 */
data class SpatialObjectFormSeed(
    val objectType: InfrastructureType = InfrastructureType.OTHER,
    val name: String = "",
    val description: String = "",
    val notes: String = "",
    val areaId: String? = null,
    val rectX1: String = "",
    val rectY1: String = "",
    val rectX2: String = "",
    val rectY2: String = "",
) {
    companion object {
        fun fromObject(spatialObject: SpatialObject): SpatialObjectFormSeed =
            SpatialObjectFormSeed(
                objectType = spatialObject.objectType,
                name = spatialObject.name ?: "",
                description = spatialObject.description ?: "",
                notes = spatialObject.notes ?: "",
                areaId = spatialObject.areaId,
            )
    }
}
