package com.mytuin.gardenplanner.domain.model.garden

/**
 * User-settable location fields for a PlantInstance.
 *
 * DEC-041 / PL5: location changes on PlantInstance are tracked in a
 * companion history table. This type is the update input for the
 * three fields whose change constitutes a location change.
 *
 * All three fields are nullable. Setting all three to null clears
 * the location (H4): a removed plant before archival, or a seedling
 * with no assigned space.
 *
 * This type is used only on the update path. The domain model
 * PlantInstance itself remains flat.
 */
data class NewPlantInstanceLocation(
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val geometry: Geometry? = null,
)
