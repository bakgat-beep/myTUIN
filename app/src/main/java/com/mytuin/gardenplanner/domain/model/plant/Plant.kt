package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.DroughtTolerance
import com.mytuin.gardenplanner.domain.vocabulary.FrostSensitivity
import com.mytuin.gardenplanner.domain.vocabulary.GrowthRate
import com.mytuin.gardenplanner.domain.vocabulary.HeatTolerance
import com.mytuin.gardenplanner.domain.vocabulary.LightRequirement
import com.mytuin.gardenplanner.domain.vocabulary.MaintenanceDemand
import com.mytuin.gardenplanner.domain.vocabulary.MaturityClassification
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PollinationRequirement
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.SalinityTolerance
import com.mytuin.gardenplanner.domain.vocabulary.ShadeTolerance
import com.mytuin.gardenplanner.domain.vocabulary.SupportRequirement
import com.mytuin.gardenplanner.domain.vocabulary.TemperatureClass
import com.mytuin.gardenplanner.domain.vocabulary.WaterloggingTolerance

/**
 * Plant — domain model.
 *
 * V1_DATABASE_SCHEMA.md §16. DATA_MODEL.md §12.
 * PLANT_VOCABULARIES.md §41 (partial — Phase 1 step 4a).
 *
 * Field names are camelCase. The Room entity uses snake_case to
 * match the schema JSON. The mapper converts between them (A28;
 * V1_TECHNICAL_ARCHITECTURE §79).
 *
 * lifecycle is nullable. The `unknown` value exists in the
 * vocabulary but "no lifecycle recorded" and "lifecycle recorded as
 * unknown" remain distinguishable (CORE_VOCABULARIES §4). The same
 * holds for every new classification added in 4a.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
data class Plant(
    val id: String,
    val canonicalName: String,
    val scientificName: String?,
    val genus: String?,
    val species: String?,
    val family: String?,
    val lifecycle: PlantLifecycle?,
    val description: String?,
    val status: RecordStatus,
    val createdAt: Long,
    val updatedAt: Long,
    val growthRate: GrowthRate?,
    val supportRequirement: SupportRequirement?,
    val lightRequirement: LightRequirement?,
    val shadeTolerance: ShadeTolerance?,
    val temperatureClass: TemperatureClass?,
    val frostSensitivity: FrostSensitivity?,
    val heatTolerance: HeatTolerance?,
    val droughtTolerance: DroughtTolerance?,
    val salinityTolerance: SalinityTolerance?,
    val waterloggingTolerance: WaterloggingTolerance?,
    val maturity: MaturityClassification?,
    val maintenanceDemand: MaintenanceDemand?,
    val pollinationRequirement: PollinationRequirement?,
    val containerSuitable: Boolean?,
)
