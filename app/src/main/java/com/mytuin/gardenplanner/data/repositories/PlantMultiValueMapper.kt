package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantMultiValueEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.vocabulary.FoodProductionCategory
import com.mytuin.gardenplanner.domain.vocabulary.GrowingMethod
import com.mytuin.gardenplanner.domain.vocabulary.GrowthHabit
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import com.mytuin.gardenplanner.domain.vocabulary.PlantRole
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.PreservationMethod
import com.mytuin.gardenplanner.domain.vocabulary.PropagationMethod
import com.mytuin.gardenplanner.domain.vocabulary.RootBehaviour
import com.mytuin.gardenplanner.domain.vocabulary.SoilDrainage
import com.mytuin.gardenplanner.domain.vocabulary.SoilTexture
import com.mytuin.gardenplanner.domain.vocabulary.SupportType
import com.mytuin.gardenplanner.domain.vocabulary.ToxicPart
import com.mytuin.gardenplanner.domain.vocabulary.WaterRequirement

fun PlantMultiValueEntity.toDomain(): PlantMultiValue {
    val id = plant_id
    val v = value_id
    val n = notes
    return when (kind) {
        PlantMultiValueKind.PLANT_ROLE -> decodeRole(id, v, n)
        PlantMultiValueKind.FOOD_PRODUCTION_CATEGORY -> decodeFoodProduction(id, v, n)
        PlantMultiValueKind.GROWTH_HABIT -> decodeHabit(id, v, n)
        PlantMultiValueKind.ROOT_BEHAVIOUR -> decodeRoot(id, v, n)
        PlantMultiValueKind.GROWING_METHOD -> decodeGrowing(id, v, n)
        PlantMultiValueKind.PROPAGATION_METHOD -> decodePropagation(id, v, n)
        PlantMultiValueKind.PLANTING_STOCK -> decodeStock(id, v, n)
        PlantMultiValueKind.WATER_REQUIREMENT -> decodeWater(id, v, n)
        PlantMultiValueKind.PREFERRED_SOIL -> decodePreferredSoil(id, v, n)
        PlantMultiValueKind.SOIL_DRAINAGE -> decodeDrainage(id, v, n)
        PlantMultiValueKind.ROTATION_GROUP -> decodeRotation(id, v, n)
        PlantMultiValueKind.TOXIC_PART -> decodeToxicPart(id, v, n)
        PlantMultiValueKind.PRESERVATION_METHOD -> decodePreservation(id, v, n)
        PlantMultiValueKind.SUPPORT_TYPE -> decodeSupport(id, v, n)
    }
}

private fun decodeRole(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Role(
        plantId = plantId,
        role = decodeOrThrow(valueId, "PlantRole", PlantRole::fromId),
        notes = notes,
    )

private fun decodeFoodProduction(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.FoodProduction(
        plantId = plantId,
        category = decodeOrThrow(valueId, "FoodProductionCategory", FoodProductionCategory::fromId),
        notes = notes,
    )

private fun decodeHabit(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Habit(
        plantId = plantId,
        habit = decodeOrThrow(valueId, "GrowthHabit", GrowthHabit::fromId),
        notes = notes,
    )

private fun decodeRoot(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Root(
        plantId = plantId,
        behaviour = decodeOrThrow(valueId, "RootBehaviour", RootBehaviour::fromId),
        notes = notes,
    )

private fun decodeGrowing(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Growing(
        plantId = plantId,
        method = decodeOrThrow(valueId, "GrowingMethod", GrowingMethod::fromId),
        notes = notes,
    )

private fun decodePropagation(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Propagation(
        plantId = plantId,
        method = decodeOrThrow(valueId, "PropagationMethod", PropagationMethod::fromId),
        notes = notes,
    )

private fun decodeStock(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Stock(
        plantId = plantId,
        stock = decodeOrThrow(valueId, "PlantingStockType", PlantingStockType::fromId),
        notes = notes,
    )

private fun decodeWater(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.WaterRequirementLevel(
        plantId = plantId,
        requirement = decodeOrThrow(valueId, "WaterRequirement", WaterRequirement::fromId),
        notes = notes,
    )

private fun decodePreferredSoil(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.PreferredSoil(
        plantId = plantId,
        texture = decodeOrThrow(valueId, "SoilTexture", SoilTexture::fromId),
        notes = notes,
    )

private fun decodeDrainage(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.SoilDrainagePreference(
        plantId = plantId,
        drainage = decodeOrThrow(valueId, "SoilDrainage", SoilDrainage::fromId),
        notes = notes,
    )

private fun decodeRotation(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Rotation(
        plantId = plantId,
        rotationGroupId = valueId,
        notes = notes,
    )

private fun decodeToxicPart(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Toxic(
        plantId = plantId,
        part = decodeOrThrow(valueId, "ToxicPart", ToxicPart::fromId),
        notes = notes,
    )

private fun decodePreservation(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Preservation(
        plantId = plantId,
        method = decodeOrThrow(valueId, "PreservationMethod", PreservationMethod::fromId),
        notes = notes,
    )

private fun decodeSupport(
    plantId: String,
    valueId: String,
    notes: String?,
): PlantMultiValue =
    PlantMultiValue.Support(
        plantId = plantId,
        supportType = decodeOrThrow(valueId, "SupportType", SupportType::fromId),
        notes = notes,
    )

private fun <T> decodeOrThrow(
    valueId: String,
    vocabularyName: String,
    decoder: (String) -> T?,
): T =
    decoder(valueId)
        ?: error("Unknown $vocabularyName id stored in plant_multi_value: '$valueId'")
