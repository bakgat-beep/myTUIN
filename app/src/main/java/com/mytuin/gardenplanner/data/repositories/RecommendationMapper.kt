package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.RecommendationEntity
import com.mytuin.gardenplanner.domain.model.recommendation.Recommendation

fun RecommendationEntity.toDomain(): Recommendation =
    Recommendation(
        id = id,
        gardenId = garden_id,
        createdAt = created_at,
        subjectType = subject_type,
        subjectId = subject_id,
        recommendationType = recommendation_type,
        status = status,
        confidence = confidence,
        overallResult = overall_result,
        expiresAt = expires_at,
        context = context,
        notes = notes,
    )

fun Recommendation.toEntity(): RecommendationEntity =
    RecommendationEntity(
        id = id,
        garden_id = gardenId,
        created_at = createdAt,
        subject_type = subjectType,
        subject_id = subjectId,
        recommendation_type = recommendationType,
        status = status,
        confidence = confidence,
        overall_result = overallResult,
        expires_at = expiresAt,
        context = context,
        notes = notes,
    )
