package com.mytuin.gardenplanner.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
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
 * Plant.
 *
 * V1_DATABASE_SCHEMA.md §16. DATA_MODEL.md §12.
 * PLANT_VOCABULARIES.md §41 (partial — Phase 1 step 4a).
 *
 * Plant is reference/knowledge data. It does not represent anything
 * growing in the user's garden (CORE_ARCHITECTURE §10, §11;
 * V1_DATABASE_SCHEMA §75 Invariant 1).
 *
 * Stable identifier format: "plant_<uuid>" (D1).
 *
 * Phase 1 step 4a adds 13 scalar classifications and one boolean to
 * the entity, drawn from PLANT_VOCABULARIES §41:
 *
 *   growth_rate, support_requirement, light_requirement,
 *   shade_tolerance, temperature_class, frost_sensitivity,
 *   heat_tolerance, drought_tolerance, salinity_tolerance,
 *   waterlogging_tolerance, maturity, maintenance_demand,
 *   pollination_requirement, container_suitable
 *
 * All nullable. Null means "not recorded"; the `unknown` value
 * within a vocabulary means "recorded as unknown" (CORE §4).
 *
 * The remaining §41 categories (multi-value classifications,
 * numeric and temporal facts, notes, relationships, rotation group)
 * arrive in subsequent sub-steps 4b–4e.
 *
 * `light_requirement` and `frost_sensitivity` are indexed (P41-4);
 * recommendations will filter on them.
 *
 * Field names use snake_case to match the schema document.
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
)
