package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.FeedingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PlantingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PruningMethod
import com.mytuin.gardenplanner.domain.vocabulary.SoilWorkMethod
import com.mytuin.gardenplanner.domain.vocabulary.WateringMethod

/**
 * Type-specific detail carried by an Activity.
 *
 * ACTIVITY_VOCABULARIES §4. The subtype that applies depends on
 * ActivityType:
 *
 *   planting   -> Planting
 *   watering   -> Watering
 *   feeding    -> Feeding
 *   pruning    -> Pruning
 *   soil_work  -> SoilWork
 *   harvesting, observation, intervention, maintenance, moving,
 *   removal    -> Plain (no subtype)
 *
 * A sealed hierarchy enforces the invariant that an activity of one
 * type cannot carry a subtype belonging to another. The mapper
 * enforces the same invariant on the persistence boundary.
 *
 * `intervention_type` is deliberately absent. ACTIVITY_VOCABULARIES
 * §3.8 defers its values to the Problem vocabulary, which does not
 * exist yet. Adding a free-text column would allow the UI to store
 * vocabulary-shaped values that have no vocabulary. Added when the
 * Problem entity lands.
 */
sealed interface ActivityDetail {
    data object Plain : ActivityDetail

    data class Planting(
        val method: PlantingMethod?,
    ) : ActivityDetail

    data class Watering(
        val method: WateringMethod?,
    ) : ActivityDetail

    data class Feeding(
        val method: FeedingMethod?,
    ) : ActivityDetail

    data class Pruning(
        val method: PruningMethod?,
    ) : ActivityDetail

    data class SoilWork(
        val method: SoilWorkMethod?,
    ) : ActivityDetail
}
