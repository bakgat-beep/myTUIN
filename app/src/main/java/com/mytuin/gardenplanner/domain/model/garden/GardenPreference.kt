package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import com.mytuin.gardenplanner.domain.vocabulary.GardenPriority

/**
 * A scalar garden-affecting preference.
 *
 * DEC-042: preferences that participate in recommendation reasoning
 * live in Room, alongside other garden data, and are exported with
 * the garden.
 *
 * Composite identity: (gardenId, key). One row per key per garden.
 * Setting the same key twice replaces the prior value.
 */
data class GardenPreference(
    val gardenId: String,
    val key: GardenPreferenceKey,
    val priority: GardenPriority,
)