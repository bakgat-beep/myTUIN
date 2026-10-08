package com.mytuin.gardenplanner.domain.identifiers

/**
 * Generator for stable entity identifiers.
 *
 * D1: prefixed UUID v4, "<prefix>_<uuid>". Prefixes name the entity
 * type. IDs must never encode display names, translations or row
 * order (V1_DATABASE_SCHEMA §5; CORE_ARCHITECTURE §51).
 *
 * The plan_target join table has no id: its identity is the
 * (plan_id, target_type, target_id) triple.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface IdGenerator {
    fun newGardenId(): String

    fun newGrowingSpaceId(): String

    fun newGrowingSpaceHistoryId(): String

    fun newAreaId(): String

    fun newSpatialObjectId(): String

    fun newPlantInstanceId(): String

    fun newPlantInstanceHistoryId(): String

    fun newActivityId(): String

    fun newObservationId(): String

    fun newMeasurementId(): String

    fun newHarvestId(): String

    fun newHarvestLossId(): String

    fun newProblemId(): String

    fun newPlanId(): String
}
