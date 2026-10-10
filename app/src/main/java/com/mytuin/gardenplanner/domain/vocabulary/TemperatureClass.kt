package com.mytuin.gardenplanner.domain.vocabulary

/**
 * TemperatureClass.
 *
 * PLANT_VOCABULARIES.md §19. Five values.
 *
 * A broad seasonal temperature preference. Actual temperature
 * requirements remain structured data and may vary by growth stage.
 */
enum class TemperatureClass(
    override val id: String,
) : VocabularyValue {
    COOL_SEASON("cool_season"),
    WARM_SEASON("warm_season"),
    INTERMEDIATE("intermediate"),
    VARIABLE("variable"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): TemperatureClass? = entries.firstOrNull { it.id == id }
    }
}
