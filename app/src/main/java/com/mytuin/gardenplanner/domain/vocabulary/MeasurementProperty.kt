package com.mytuin.gardenplanner.domain.vocabulary

/**
 * MeasurementProperty.
 *
 * V1_DATABASE_SCHEMA §32. M2 (a): a fixed enum bounded to what V1
 * needs. If the set grows unboundedly (plant-specific properties,
 * user-defined properties), it converts to reference data per
 * VOCABULARY_INDEX §3.2.
 *
 * Each value is a property of a site or a medium, not of a plant.
 * A temperature reading on a specific plant is still a temperature;
 * the plant_instance_id column carries the subject.
 */
enum class MeasurementProperty(
    override val id: String,
) : VocabularyValue {
    PH("ph"),
    TEMPERATURE("temperature"),
    MOISTURE("moisture"),
    HUMIDITY("humidity"),
    LIGHT_INTENSITY("light_intensity"),
    SUNLIGHT_HOURS("sunlight_hours"),
    DRAINAGE_TIME("drainage_time"),
    ELECTRICAL_CONDUCTIVITY("electrical_conductivity"),
    ORGANIC_MATTER("organic_matter"),
    RAINFALL("rainfall"),
    WIND_SPEED("wind_speed"),
    NITROGEN("nitrogen"),
    PHOSPHORUS("phosphorus"),
    POTASSIUM("potassium"),
    CALCIUM("calcium"),
    MAGNESIUM("magnesium"),
    SULFUR("sulfur"),
    ;

    companion object {
        fun fromId(id: String): MeasurementProperty? = entries.firstOrNull { it.id == id }
    }
}
