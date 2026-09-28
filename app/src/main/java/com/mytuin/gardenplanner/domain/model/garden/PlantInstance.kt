package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * PlantInstance — domain model.
 *
 * V1_DATABASE_SCHEMA §19. DATA_MODEL §15–§18.
 *
 * Represents an actual or intended occurrence of a Plant in the
 * user's garden. Not a Plant (knowledge). Not evidence that planting
 * occurred — that requires an actual planting Activity (Invariant 2,
 * §75).
 *
 * `status` is the record's lifecycle (RecordStatus). `lifecycle` is
 * the plant's (PlantInstanceLifecycle). The two are orthogonal; a
 * record may be active while its lifecycle is harvested.
 *
 * `geometry` is optional and gives a PlantInstance its own location
 * independent of any GrowingSpace or SpatialObject. Resolves the
 * tree case raised in B3.
 */
data class PlantInstance(
    val id: String,
    val gardenId: String,
    val plantId: String,
    val status: RecordStatus,
    val cultivarId: String?,
    val growingSpaceId: String?,
    val spatialObjectId: String?,
    val name: String?,
    val quantity: Int?,
    val plannedDate: KnownDate?,
    val plantedDate: KnownDate?,
    val expectedEndDate: KnownDate?,
    val removedDate: KnownDate?,
    val plantingStock: PlantingStockType?,
    val lifecycle: PlantInstanceLifecycle?,
    val geometry: Geometry?,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
