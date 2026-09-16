package com.mytuin.gardenplanner.domain.identifiers

/**
 * Generator for stable entity identifiers.
 *
 * D1: prefixed UUID v4, "<prefix>_<uuid>". Prefixes name the entity
 * type. IDs must never encode display names, translations or row
 * order (V1_DATABASE_SCHEMA §5; CORE_ARCHITECTURE §51).
 *
 * Interface so tests can inject a deterministic generator. The
 * production implementation lives in platform/identifiers/.
 *
 * S2 precedent: methods are added when their first write path
 * arrives. newGrowingSpaceId() arrives with step 5c.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface IdGenerator {
    fun newGardenId(): String
    fun newGrowingSpaceId(): String
}