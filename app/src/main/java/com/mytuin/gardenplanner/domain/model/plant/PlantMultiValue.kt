package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.FoodProductionCategory
import com.mytuin.gardenplanner.domain.vocabulary.GrowingMethod
import com.mytuin.gardenplanner.domain.vocabulary.GrowthHabit
import com.mytuin.gardenplanner.domain.vocabulary.PlantRole
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.PropagationMethod
import com.mytuin.gardenplanner.domain.vocabulary.RootBehaviour

/**
 * A multi-valued classification attached to a Plant.
 *
 * PLANT_VOCABULARIES §41 (multi-value fields). Stored in the
 * plant_multi_value join table with a kind discriminator (P41B-4).
 *
 * The sealed hierarchy gives the caller typed access to the decoded
 * value. The mapper enforces the kind-to-value correspondence; a
 * stored value_id that is not legal for its kind is a
 * data-integrity error, not a silently dropped row.
 *
 * PlantInstance.plantingStock is a distinct concept (P41B-3): that
 * records what was actually planted for one instance, whereas
 * PlantMultiValue.Stock records the forms in which a plant can
 * commonly be established.
 */
sealed interface PlantMultiValue {
    val plantId: String
    val notes: String?

    data class Role(
        override val plantId: String,
        val role: PlantRole,
        override val notes: String?,
    ) : PlantMultiValue

    data class FoodProduction(
        override val plantId: String,
        val category: FoodProductionCategory,
        override val notes: String?,
    ) : PlantMultiValue

    data class Habit(
        override val plantId: String,
        val habit: GrowthHabit,
        override val notes: String?,
    ) : PlantMultiValue

    data class Root(
        override val plantId: String,
        val behaviour: RootBehaviour,
        override val notes: String?,
    ) : PlantMultiValue

    data class Growing(
        override val plantId: String,
        val method: GrowingMethod,
        override val notes: String?,
    ) : PlantMultiValue

    data class Propagation(
        override val plantId: String,
        val method: PropagationMethod,
        override val notes: String?,
    ) : PlantMultiValue

    data class Stock(
        override val plantId: String,
        val stock: PlantingStockType,
        override val notes: String?,
    ) : PlantMultiValue
}
