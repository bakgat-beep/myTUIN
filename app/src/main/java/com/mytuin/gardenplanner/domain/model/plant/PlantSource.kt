package com.mytuin.gardenplanner.domain.model.plant

import com.mytuin.gardenplanner.domain.vocabulary.SourceRelationship

/**
 * A link between a Plant and a Source.
 *
 * V1_DATABASE_SCHEMA §24. PLANT_VOCABULARIES §41 `sources`.
 *
 * Identity is (plantId, sourceId). `sourceRelationship` describes
 * how the source supports the plant record: directly quoted,
 * adapted, derived, summarised, inferred, or unknown.
 */
data class PlantSource(
    val plantId: String,
    val sourceId: String,
    val sourceRelationship: SourceRelationship?,
    val notes: String?,
)
