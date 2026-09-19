package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.model.garden.Garden

/**
 * Mapping between GardenEntity (snake_case, schema-aligned) and
 * Garden (camelCase, idiomatic Kotlin). Bidirectional because the
 * write path now exists.
 */

fun GardenEntity.toDomain(): Garden =
    Garden(
        id = id,
        name = name,
        description = description,
        countryCode = country_code,
        region = region,
        locality = locality,
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        hemisphere = hemisphere,
        status = status,
        createdAt = created_at,
        updatedAt = updated_at,
    )

fun Garden.toEntity(): GardenEntity =
    GardenEntity(
        id = id,
        name = name,
        description = description,
        country_code = countryCode,
        region = region,
        locality = locality,
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        hemisphere = hemisphere,
        status = status,
        created_at = createdAt,
        updated_at = updatedAt,
    )
