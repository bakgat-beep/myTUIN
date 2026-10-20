package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.RecommendationFactorEntity
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationFactor

fun RecommendationFactorEntity.toDomain(): RecommendationFactor =
    RecommendationFactor(
        id = id,
        recommendationId = recommendation_id,
        dimension = dimension,
        result = result,
        confidence = confidence,
        weight = weight,
        reasonType = reason_type,
        notes = notes,
    )

fun RecommendationFactor.toEntity(): RecommendationFactorEntity =
    RecommendationFactorEntity(
        id = id,
        recommendation_id = recommendationId,
        dimension = dimension,
        result = result,
        confidence = confidence,
        weight = weight,
        reason_type = reasonType,
        notes = notes,
    )
