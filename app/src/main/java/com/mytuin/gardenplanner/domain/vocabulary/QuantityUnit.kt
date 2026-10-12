package com.mytuin.gardenplanner.domain.vocabulary

/**
 * QuantityUnit.
 *
 * Unified unit vocabulary for every numeric value in the app:
 * activity quantities, measurements, and (from step 4c-B) plant
 * numeric facts. Replaces the two prior vocabularies
 * ActivityQuantityUnit (step 3a) and MeasurementUnit (step 3c),
 * which overlapped without semantic difference.
 *
 * DEC-046 records the unification. Existing ids are preserved
 * wherever possible; only the hour/hours and minute/minutes
 * collisions were resolved, in favour of the singular form that
 * Activity already used.
 *
 * Ids are canonical English identifiers. Display labels are
 * provided through the localisation layer.
 */
enum class QuantityUnit(
    override val id: String,
) : VocabularyValue {
    BASKET("basket"),
    BUNCH("bunch"),
    CELSIUS("celsius"),
    CENTIMETRE("centimetre"),
    CONTAINER("container"),
    COUNT("count"),
    DAY("day"),
    DECISIEMENS_PER_METRE("decisiemens_per_metre"),
    FAHRENHEIT("fahrenheit"),
    GRAM("gram"),
    HOUR("hour"),
    INCHES("inches"),
    KILOGRAM("kilogram"),
    LITRE("litre"),
    LUX("lux"),
    METRE("metre"),
    METRES_PER_SECOND("metres_per_second"),
    MILLIGRAMS_PER_KILOGRAM("milligrams_per_kilogram"),
    MILLILITRE("millilitre"),
    MILLIMETRES("millimetres"),
    MILLISIEMENS_PER_CENTIMETRE("millisiemens_per_centimetre"),
    MINUTE("minute"),
    OTHER("other"),
    PARTS_PER_MILLION("parts_per_million"),
    PERCENT("percent"),
    PH("ph"),
    SQUARE_METRE("square_metre"),
    WEEK("week"),
    ;

    companion object {
        fun fromId(id: String): QuantityUnit? = entries.firstOrNull { it.id == id }
    }
}
