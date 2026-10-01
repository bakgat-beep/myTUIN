package com.mytuin.gardenplanner.domain.vocabulary

/**
 * WateringMethod.
 *
 * ACTIVITY_VOCABULARIES.md §6. Values exactly as listed.
 */
enum class WateringMethod(
    override val id: String,
) : VocabularyValue {
    HAND_WATERED("hand_watered"),
    HOSE("hose"),
    WATERING_CAN("watering_can"),
    DRIP("drip"),
    SPRINKLER("sprinkler"),
    IRRIGATION_SYSTEM("irrigation_system"),
    RAIN("rain"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): WateringMethod? = entries.firstOrNull { it.id == id }
    }
}
