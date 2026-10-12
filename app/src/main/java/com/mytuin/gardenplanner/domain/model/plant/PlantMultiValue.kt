package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.FoodProductionCategory
import com.mytuin.gardenplanner.domain.vocabulary.GrowingMethod
import com.mytuin.gardenplanner.domain.vocabulary.GrowthHabit
import com.mytuin.gardenplanner.domain.vocabulary.PlantRole
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.PropagationMethod
import com.mytuin.gardenplanner.domain.vocabulary.RootBehaviour
import com.mytuin.gardenplanner.domain.vocabulary.SoilDrainage
import com.mytuin.gardenplanner.domain.vocabulary.SoilTexture
import com.mytuin.gardenplanner.domain.vocabulary.WaterRequirement

/**
 * A multi-valued classification attached to a Plant.
 *
 * PLANT_VOCABULARIES §41 (multi-value fields). Stored in the
 * plant_multi_value join table with a kind discriminator (P41B-4).
 *
 * Step 4c-B added three variants: WaterRequirement, PreferredSoil
 * and SoilDrainage (P41C-2).
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

    data class WaterRequirementLevel(
        override val plantId: String,
        val requirement: WaterRequirement,
        override val notes: String?,
    ) : PlantMultiValue

    data class PreferredSoil(
        override val plantId: String,
        val texture: SoilTexture,
        override val notes: String?,
    ) : PlantMultiValue

    data class SoilDrainagePreference(
        override val plantId: String,
        val drainage: SoilDrainage,
        override val notes: String?,
    ) : PlantMultiValue
}
