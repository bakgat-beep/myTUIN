package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantRelationshipEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantRelationship

/**
 * Mapping between PlantRelationshipEntity and PlantRelationship.
 *
 * Invariant (P41D-7): plant_id != related_plant_id. A plant cannot
 * have a relationship with itself.
 */

fun PlantRelationshipEntity.toDomain(): PlantRelationship {
    require(plant_id != related_plant_id) {
        "PlantRelationship cannot relate plant '$plant_id' to itself"
    }
    return PlantRelationship(
        plantId = plant_id,
        relatedPlantId = related_plant_id,
        relationshipType = relationship_type,
        strength = strength,
        reason = reason,
        sourceId = source_id,
        confidence = confidence,
        notes = notes,
    )
}
