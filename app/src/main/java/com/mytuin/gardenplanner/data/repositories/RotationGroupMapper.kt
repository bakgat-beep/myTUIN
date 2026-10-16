package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.RotationGroupEntity
import com.mytuin.gardenplanner.domain.model.plant.RotationGroup
import com.mytuin.gardenplanner.domain.vocabulary.RotationGroupType

/**
 * Mapping between RotationGroupEntity and RotationGroup.
 *
 * Invariant (P41E-1 (c)): system groups have null garden_id;
 * user-defined groups have non-null garden_id. Enforced at read
 * time.
 */
fun RotationGroupEntity.toDomain(): RotationGroup {
    when (group_type) {
        RotationGroupType.SYSTEM ->
            require(garden_id == null) {
                "System rotation group '$id' must have null garden_id"
            }
        RotationGroupType.USER_DEFINED ->
            require(garden_id != null) {
                "User-defined rotation group '$id' must have a non-null garden_id"
            }
    }
    return RotationGroup(
        id = id,
        name = name,
        groupType = group_type,
        status = status,
        gardenId = garden_id,
        description = description,
        createdAt = created_at,
        updatedAt = updated_at,
    )
}
