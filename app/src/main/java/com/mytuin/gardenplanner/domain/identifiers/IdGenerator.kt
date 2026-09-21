package com.mytuin.gardenplanner.domain.identifiers

/**
 * Generator for stable entity identifiers.
 *
 * D1: prefixed UUID v4, "<prefix>_<uuid>". Prefixes name the entity
 * type. IDs must never encode display names, translations or row
 * order (V1_DATABASE_SCHEMA §5; CORE_ARCHITECTURE §51).
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface IdGenerator {
    fun newGardenId(): String

    fun newGrowingSpaceId(): String

    fun newGrowingSpaceHistoryId(): String

    fun newAreaId(): String
}
