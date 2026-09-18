package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Scalar garden-affecting preference keys.
 *
 * DEC-042 lists eight preferences. Two of them (favourite plants,
 * plants to avoid) reference Plant and are modelled by
 * GardenPlantPreferenceKind, not here. One (space utilisation) is
 * excluded per A174=b. Six remain.
 *
 * A187.
 */
enum class GardenPreferenceKey(override val id: String) : VocabularyValue {
    WATER_CONSERVATION_PRIORITY("water_conservation_priority"),
    POLLINATOR_PRIORITY("pollinator_priority"),
    LOW_MAINTENANCE_PREFERENCE("low_maintenance_preference"),
    FOOD_PRODUCTION_PRIORITY("food_production_priority"),
    NATIVE_PLANT_PREFERENCE("native_plant_preference"),
    EXPERIMENTATION_PREFERENCE("experimentation_preference"),
    ;

    companion object {
        fun fromId(id: String): GardenPreferenceKey? =
            entries.firstOrNull { it.id == id }
    }
}