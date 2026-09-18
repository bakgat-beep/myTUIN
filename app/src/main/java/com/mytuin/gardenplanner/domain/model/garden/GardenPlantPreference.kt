package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind

/**
 * A plant flagged as favourite or to-avoid within a garden.
 *
 * DEC-042 names both preferences. They share one table with a kind
 * discriminator (A176=a).
 *
 * Composite identity: (gardenId, plantId, kind).
 */
data class GardenPlantPreference(
    val gardenId: String,
    val plantId: String,
    val kind: GardenPlantPreferenceKind,
)