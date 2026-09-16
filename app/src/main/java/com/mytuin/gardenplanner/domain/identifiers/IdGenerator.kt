package com.mytuin.gardenplanner.domain.identifiers

/**
 * Generator for stable entity identifiers.
 *
 * D1: prefixed UUID v4. Format is "<prefix>_<uuid>", where prefix
 * names the entity type. IDs must not encode display names, translations
 * or row order (V1_DATABASE_SCHEMA §5; CORE_ARCHITECTURE §51).
 *
 * Interface so tests can inject a deterministic generator. The
 * production implementation lives in platform/identifiers/.
 *
 * Only newGardenId() exists at step 5b. Other entity methods are added
 * when their first write path arrives.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface IdGenerator {
    fun newGardenId(): String
}