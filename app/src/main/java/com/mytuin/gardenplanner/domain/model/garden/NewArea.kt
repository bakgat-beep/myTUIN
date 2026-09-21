package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.AreaType

/**
 * User-settable fields for a new Area.
 *
 * The use case assigns id, status, createdAt and updatedAt, so they
 * are absent here. Same shape as NewGrowingSpace.
 */
data class NewArea(
    val gardenId: String,
    val name: String,
    val areaType: AreaType,
    val description: String? = null,
    val geometry: Geometry? = null,
)
