package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.PlantRelationshipType

/**
 * A knowledge relationship between two Plants.
 *
 * V1_DATABASE_SCHEMA §22. PLANT_VOCABULARIES §29.
 *
 * Identity is (plantId, relatedPlantId, relationshipType). The
 * mapper enforces plantId != relatedPlantId (P41D-7).
 *
 * This is knowledge, not a user observation. That a plant has a
 * documented companion relationship does not mean the user has
 * observed that companion effect in their garden.
 */
data class PlantRelationship(
    val plantId: String,
    val relatedPlantId: String,
    val relationshipType: PlantRelationshipType,
    val strength: String?,
    val reason: String?,
    val sourceId: String?,
    val confidence: Confidence?,
    val notes: String?,
)
