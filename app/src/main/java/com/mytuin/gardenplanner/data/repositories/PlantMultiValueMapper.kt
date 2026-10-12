package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantMultiValueEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.vocabulary.FoodProductionCategory
import com.mytuin.gardenplanner.domain.vocabulary.GrowingMethod
import com.mytuin.gardenplanner.domain.vocabulary.GrowthHabit
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import com.mytuin.gardenplanner.domain.vocabulary.PlantRole
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.PropagationMethod
import com.mytuin.gardenplanner.domain.vocabulary.RootBehaviour

/**
 * Decodes a PlantMultiValueEntity row into a typed PlantMultiValue
 * variant, based on the kind discriminator.
 *
 * A stored value_id that is not a legal value for its kind is a
 * data-integrity error and throws. This is the analogue of
 * ActivityMapper's subtype validation (Step 3a) and
 * PlanRepositoryImpl.requireTargetExists (Step 3f): the database
 * cannot enforce the pair, so the mapper does.
 */
fun PlantMultiValueEntity.toDomain(): PlantMultiValue =
    when (kind) {
        PlantMultiValueKind.PLANT_ROLE ->
            PlantMultiValue.Role(
                plantId = plant_id,
                role = decodeOrThrow(value_id, "PlantRole", PlantRole::fromId),
                notes = notes,
            )
        PlantMultiValueKind.FOOD_PRODUCTION_CATEGORY ->
            PlantMultiValue.FoodProduction(
                plantId = plant_id,
                category =
                    decodeOrThrow(
                        value_id,
                        "FoodProductionCategory",
                        FoodProductionCategory::fromId,
                    ),
                notes = notes,
            )
        PlantMultiValueKind.GROWTH_HABIT ->
            PlantMultiValue.Habit(
                plantId = plant_id,
                habit = decodeOrThrow(value_id, "GrowthHabit", GrowthHabit::fromId),
                notes = notes,
            )
        PlantMultiValueKind.ROOT_BEHAVIOUR ->
            PlantMultiValue.Root(
                plantId = plant_id,
                behaviour = decodeOrThrow(value_id, "RootBehaviour", RootBehaviour::fromId),
                notes = notes,
            )
        PlantMultiValueKind.GROWING_METHOD ->
            PlantMultiValue.Growing(
                plantId = plant_id,
                method = decodeOrThrow(value_id, "GrowingMethod", GrowingMethod::fromId),
                notes = notes,
            )
        PlantMultiValueKind.PROPAGATION_METHOD ->
            PlantMultiValue.Propagation(
                plantId = plant_id,
                method =
                    decodeOrThrow(
                        value_id,
                        "PropagationMethod",
                        PropagationMethod::fromId,
                    ),
                notes = notes,
            )
        PlantMultiValueKind.PLANTING_STOCK ->
            PlantMultiValue.Stock(
                plantId = plant_id,
                stock = decodeOrThrow(value_id, "PlantingStockType", PlantingStockType::fromId),
                notes = notes,
            )
    }

private fun <T> decodeOrThrow(
    valueId: String,
    vocabularyName: String,
    decoder: (String) -> T?,
): T =
    decoder(valueId)
        ?: error("Unknown $vocabularyName id stored in plant_multi_value: '$valueId'")
