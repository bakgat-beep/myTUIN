package com.mytuin.gardenplanner.domain.model.garden

import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

/**
 * GrowingSpace — domain model.
 *
 * V1_DATABASE_SCHEMA.md §13. DATA_MODEL.md §8.
 *
 * Dimensions are in metres (A51=A; DEC-042; CORE_ARCHITECTURE §59).
 * Suffixing the field names with "Metres"/"SquareMetres"/"CubicMetres"
 * makes the canonical unit explicit at every call site. No unit column
 * exists; conversion to display units is a presentation concern.
 *
 * geometry is nullable: a space may exist before its shape has been
 * drawn.
 *
 * areaId is nullable. V1_DATABASE_SCHEMA §13 lists it under Optional
 * fields; A3 confirms FK is RESTRICT. Placement after description
 * matches SpatialObject (Step 1b), keeping the optional spatial parent
 * near the other optional fields.
 */
data class GrowingSpace(
    val id: String,
    val gardenId: String,
    val name: String,
    val spaceType: GrowingSpaceType,
    val status: RecordStatus,
    val geometry: Geometry?,
    val lengthMetres: Double?,
    val widthMetres: Double?,
    val heightMetres: Double?,
    val diameterMetres: Double?,
    val areaSquareMetres: Double?,
    val volumeCubicMetres: Double?,
    val description: String?,
    val areaId: String?,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
