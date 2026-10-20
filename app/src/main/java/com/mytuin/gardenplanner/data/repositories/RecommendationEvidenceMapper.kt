package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.RecommendationEvidenceEntity
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationEvidence

fun RecommendationEvidenceEntity.toDomain(): RecommendationEvidence =
    RecommendationEvidence(
        id = id,
        recommendationId = recommendation_id,
        sourceType = source_type,
        sourceId = source_id,
        role = role,
        notes = notes,
    )

fun RecommendationEvidence.toEntity(): RecommendationEvidenceEntity =
    RecommendationEvidenceEntity(
        id = id,
        recommendation_id = recommendationId,
        source_type = sourceType,
        source_id = sourceId,
        role = role,
        notes = notes,
    )
