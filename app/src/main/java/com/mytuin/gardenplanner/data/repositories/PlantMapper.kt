package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.domain.model.plant.Cultivar
import com.mytuin.gardenplanner.domain.model.plant.Plant
import com.mytuin.gardenplanner.domain.model.plant.PlantAlias

fun PlantEntity.toDomain(): Plant =
    Plant(
        id = id,
        canonicalName = canonical_name,
        scientificName = scientific_name,
        genus = genus,
        species = species,
        family = family,
        lifecycle = lifecycle,
        description = description,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
        growthRate = growth_rate,
        supportRequirement = support_requirement,
        lightRequirement = light_requirement,
        shadeTolerance = shade_tolerance,
        temperatureClass = temperature_class,
        frostSensitivity = frost_sensitivity,
        heatTolerance = heat_tolerance,
        droughtTolerance = drought_tolerance,
        salinityTolerance = salinity_tolerance,
        waterloggingTolerance = waterlogging_tolerance,
        maturity = maturity,
        maintenanceDemand = maintenance_demand,
        pollinationRequirement = pollination_requirement,
        containerSuitable = container_suitable,
        fertilityRequirement = fertility_requirement,
        toxicity = toxicity,
    )

fun PlantAliasEntity.toDomain(): PlantAlias =
    PlantAlias(
        id = id,
        plantId = plant_id,
        alias = alias,
        aliasType = alias_type,
        language = language,
    )

fun CultivarEntity.toDomain(): Cultivar =
    Cultivar(
        id = id,
        plantId = plant_id,
        name = name,
        description = description,
        notes = notes,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
    )
