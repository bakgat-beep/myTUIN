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
 * drawn. Not all growing spaces (a lawn, a row) will necessarily be
 * given a closed polygon.
 *
 * area_id is deliberately absent (A54). Area does not exist yet.
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
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
)
