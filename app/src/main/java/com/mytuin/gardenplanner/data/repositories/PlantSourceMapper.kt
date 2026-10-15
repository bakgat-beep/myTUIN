package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantSourceEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantSource

fun PlantSourceEntity.toDomain(): PlantSource =
    PlantSource(
        plantId = plant_id,
        sourceId = source_id,
        sourceRelationship = source_relationship,
        notes = notes,
    )
