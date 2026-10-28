package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * The mutable scalar fields of a SpatialObject (Step 9e).
 *
 * Geometry is deliberately absent. Geometry changes on SpatialObject
 * would need a companion history table (DEC-041), which 9i owns.
 * 9e supports scalar edits only: objectType, name, description,
 * areaId (for reassignment between areas), notes.
 *
 * Area reassignment is the reason this path exists: moving a
 * transportable object (bench, water butt) to a different area is a
 * routine operation, and forcing the user into 9g's map to do it
 * would be a regression against 9c's non-map editing of growing
 * spaces.
 */
data class SpatialObjectFields(
    val objectType: InfrastructureType,
    val name: String?,
    val description: String?,
    val areaId: String?,
    val notes: String?,
)
