package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.GrowthStage
import com.mytuin.gardenplanner.domain.vocabulary.PlantProblemRelationshipType
import com.mytuin.gardenplanner.domain.vocabulary.ProblemResponse
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity

/**
 * A knowledge relationship between a Plant and a Problem.
 *
 * V1_DATABASE_SCHEMA §23. PLANT_VOCABULARIES §27.
 *
 * Identity is (plantId, problemId, relationshipType). `cultivarId`
 * is non-key (P41D-9): one row per (plant, problem, relationship
 * type). If a future requirement surfaces for both a species-level
 * and a cultivar-level row of the same type, a surrogate key is
 * added in a follow-up migration.
 *
 * `relationshipType` (3 values) describes the nature of the link.
 * `responseCharacteristic` (7 values) describes how the plant
 * responds, when known. Both may be present.
 *
 * This is knowledge about a plant. It does not mean the user's
 * plant currently has the problem.
 */
data class PlantProblemRelationship(
    val plantId: String,
    val problemId: String,
    val relationshipType: PlantProblemRelationshipType,
    val cultivarId: String?,
    val responseCharacteristic: ProblemResponse?,
    val growthStage: GrowthStage?,
    val severity: ProblemSeverity?,
    val reason: String?,
    val sourceId: String?,
    val confidence: Confidence?,
    val notes: String?,
)
