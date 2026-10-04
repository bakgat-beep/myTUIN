package com.mytuin.gardenplanner.domain.vocabulary

/**
 * MeasurementUnit.
 *
 * V1_DATABASE_SCHEMA §32. M3 (a): a dedicated enum, separate from
 * ActivityQuantityUnit (ACTIVITY_VOCABULARIES §20). The two sets do
 * not overlap: ActivityQuantityUnit has no celsius, no percent, no
 * concentration units.
 *
 * `ph` is a conceptual unit for a dimensionless quantity. Its
 * presence ensures the unit column is never null for a pH reading.
 * If a future decision prefers a null unit for dimensionless values,
 * that is a schema change to §32.
 *
 * Units are canonical English identifiers. Display labels are
 * provided through the localisation layer.
 */
enum class MeasurementUnit(
    override val id: String,
) : VocabularyValue {
    PH("ph"),
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit"),
    PERCENT("percent"),
    HOURS("hours"),
    MINUTES("minutes"),
    LUX("lux"),
    MILLIGRAMS_PER_KILOGRAM("milligrams_per_kilogram"),
    PARTS_PER_MILLION("parts_per_million"),
    DECISIEMENS_PER_METRE("decisiemens_per_metre"),
    MILLISIEMENS_PER_CENTIMETRE("millisiemens_per_centimetre"),
    MILLIMETRES("millimetres"),
    INCHES("inches"),
    METRES_PER_SECOND("metres_per_second"),
    ;

    companion object {
        fun fromId(id: String): MeasurementUnit? = entries.firstOrNull { it.id == id }
    }
}
