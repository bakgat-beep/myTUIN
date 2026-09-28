package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantInstanceLifecycle.
 *
 * PLANT_VOCABULARIES.md §3a (v0.5). Eight values. No `unknown`:
 * absence of a lifecycle is represented by a nullable column, which
 * CORE_VOCABULARIES §4 distinguishes from "recorded as unknown".
 *
 * Separate from RecordStatus (§20 of V1_DATABASE_SCHEMA; §3 of
 * PLANT_VOCABULARIES v0.5). Status is the record's lifecycle;
 * lifecycle is the plant's.
 */
enum class PlantInstanceLifecycle(
    override val id: String,
) : VocabularyValue {
    PLANNED("planned"),
    PLANTED("planted"),
    ESTABLISHED("established"),
    DORMANT("dormant"),
    HARVESTED("harvested"),
    REMOVED("removed"),
    FAILED("failed"),
    COMPLETED("completed"),
    ;

    companion object {
        fun fromId(id: String): PlantInstanceLifecycle? = entries.firstOrNull { it.id == id }
    }
}
