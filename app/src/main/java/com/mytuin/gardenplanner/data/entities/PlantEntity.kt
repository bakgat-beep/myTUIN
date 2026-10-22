package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mytuin.gardenplanner.domain.vocabulary.DroughtTolerance
import com.mytuin.gardenplanner.domain.vocabulary.FertilityRequirement
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
import com.mytuin.gardenplanner.domain.vocabulary.Toxicity
import com.mytuin.gardenplanner.domain.vocabulary.WaterloggingTolerance

/**
 * Plant.
 *
 * V1_DATABASE_SCHEMA §16. PLANT_VOCABULARIES §41 (complete after
 * Phase 1 step 8).
 */
@Entity(
    tableName = "plant",
    indices = [
        Index(value = ["light_requirement"]),
        Index(value = ["frost_sensitivity"]),
    ],
)
data class PlantEntity(
    @PrimaryKey
    val id: String,
    val canonical_name: String,
    val created_at: Long,
    val updated_at: Long,
    val status: RecordStatus,
    val scientific_name: String? = null,
    val genus: String? = null,
    val species: String? = null,
    val family: String? = null,
    val lifecycle: PlantLifecycle? = null,
    val description: String? = null,
    val growth_rate: GrowthRate? = null,
    val support_requirement: SupportRequirement? = null,
    val light_requirement: LightRequirement? = null,
    val shade_tolerance: ShadeTolerance? = null,
    val temperature_class: TemperatureClass? = null,
    val frost_sensitivity: FrostSensitivity? = null,
    val heat_tolerance: HeatTolerance? = null,
    val drought_tolerance: DroughtTolerance? = null,
    val salinity_tolerance: SalinityTolerance? = null,
    val waterlogging_tolerance: WaterloggingTolerance? = null,
    val maturity: MaturityClassification? = null,
    val maintenance_demand: MaintenanceDemand? = null,
    val pollination_requirement: PollinationRequirement? = null,
    val container_suitable: Boolean? = null,
    val fertility_requirement: FertilityRequirement? = null,
    val toxicity: Toxicity? = null,
)
