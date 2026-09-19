package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Priority of a garden-affecting preference.
 *
 * PLANNING_VOCABULARIES.md §16. A173 overridden: the values are
 * low, normal, high, critical. GARDEN_VOCABULARIES §14 (very_low,
 * low, moderate, high, very_high, unknown) was the initial choice but
 * describes condition ratings, not priorities.
 *
 * No unknown value. Absence of a preference row means "not set",
 * which is a distinct state from "set to a low priority".
 *
 * Canonical ids only (DEC-040).
 */
enum class GardenPriority(
    override val id: String,
) : VocabularyValue {
    LOW("low"),
    NORMAL("normal"),
    HIGH("high"),
    CRITICAL("critical"),
    ;

    companion object {
        fun fromId(id: String): GardenPriority? = entries.firstOrNull { it.id == id }
    }
}
