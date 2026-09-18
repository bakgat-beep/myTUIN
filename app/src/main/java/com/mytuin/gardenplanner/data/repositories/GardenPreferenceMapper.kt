package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.GardenPlantPreferenceEntity
import com.mytuin.gardenplanner.data.entities.GardenPreferenceEntity
import com.mytuin.gardenplanner.domain.model.garden.GardenPlantPreference
import com.mytuin.gardenplanner.domain.model.garden.GardenPreference

fun GardenPreferenceEntity.toDomain(): GardenPreference = GardenPreference(
    gardenId = garden_id,
    key = preference_key,
    priority = priority,
)

fun GardenPreference.toEntity(): GardenPreferenceEntity = GardenPreferenceEntity(
    garden_id = gardenId,
    preference_key = key,
    priority = priority,
)

fun GardenPlantPreferenceEntity.toDomain(): GardenPlantPreference =
    GardenPlantPreference(
        gardenId = garden_id,
        plantId = plant_id,
        kind = kind,
    )

fun GardenPlantPreference.toEntity(): GardenPlantPreferenceEntity =
    GardenPlantPreferenceEntity(
        garden_id = gardenId,
        plant_id = plantId,
        kind = kind,
    )