package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType

/**
 * User-settable fields for a new PlantInstance.
 *
 * The use case assigns id, status, createdAt and updatedAt.
 * garden_id and plant_id are required (§19).
 */
data class NewPlantInstance(
    val gardenId: String,
    val plantId: String,
    val cultivarId: String? = null,
    val growingSpaceId: String? = null,
    val spatialObjectId: String? = null,
    val name: String? = null,
    val quantity: Int? = null,
    val plannedDate: KnownDate? = null,
    val plantedDate: KnownDate? = null,
    val expectedEndDate: KnownDate? = null,
    val removedDate: KnownDate? = null,
    val plantingStock: PlantingStockType? = null,
    val lifecycle: PlantInstanceLifecycle? = null,
    val geometry: Geometry? = null,
    val notes: String? = null,
)
