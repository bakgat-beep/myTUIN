package com.mytuin.gardenplanner.domain.vocabulary

/**
 * HarvestLossCause.
 *
 * ACTIVITY_VOCABULARIES.md §22. All 9 values included.
 *
 * `unknown` is a value: it distinguishes "the cause was recorded as
 * unknown" from "no cause was recorded" (null). CORE_VOCABULARIES
 * §4.
 */
enum class HarvestLossCause(
    override val id: String,
) : VocabularyValue {
    ROT("rot"),
    PEST("pest"),
    DISEASE("disease"),
    WEATHER("weather"),
    PHYSICAL_DAMAGE("physical_damage"),
    OVERRIPE("overripe"),
    ANIMAL("animal"),
    UNKNOWN("unknown"),
    OTHER("other"),
    ;

    companion object {
        fun fromId(id: String): HarvestLossCause? = entries.firstOrNull { it.id == id }
    }
}
