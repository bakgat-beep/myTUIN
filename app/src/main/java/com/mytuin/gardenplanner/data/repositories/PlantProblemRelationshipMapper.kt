package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.PlantProblemRelationshipEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantProblemRelationship

fun PlantProblemRelationshipEntity.toDomain(): PlantProblemRelationship =
    PlantProblemRelationship(
        plantId = plant_id,
        problemId = problem_id,
        relationshipType = relationship_type,
        cultivarId = cultivar_id,
        responseCharacteristic = response_characteristic,
        growthStage = growth_stage,
        severity = severity,
        reason = reason,
        sourceId = source_id,
        confidence = confidence,
        notes = notes,
    )
