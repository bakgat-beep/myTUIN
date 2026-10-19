package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.SoilRecordEntity
import com.mytuin.gardenplanner.domain.model.garden.SoilRecord

/**
 * Mapping between SoilRecordEntity and SoilRecord.
 *
 * Invariant (S5-1 (a)): at most one of area_id and growing_space_id
 * is set. SQLite cannot express this through Room's @Entity, so the
 * mapper enforces it at read time.
 */

fun SoilRecordEntity.toDomain(): SoilRecord {
    require(area_id == null || growing_space_id == null) {
        "SoilRecord '$id' must not have both area_id and growing_space_id set; " +
            "got area_id=$area_id, growing_space_id=$growing_space_id"
    }
    return SoilRecord(
        id = id,
        gardenId = garden_id,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
        areaId = area_id,
        growingSpaceId = growing_space_id,
        name = name,
        notes = notes,
    )
}

fun SoilRecord.toEntity(): SoilRecordEntity =
    SoilRecordEntity(
        id = id,
        garden_id = gardenId,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
        area_id = areaId,
        growing_space_id = growingSpaceId,
        name = name,
        notes = notes,
    )
