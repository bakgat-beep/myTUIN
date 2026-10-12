package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantNumericFactKind.
 *
 * Discriminator for the plant_numeric_fact table (step 4c-B). Each
 * kind names one of the §41 numeric fields whose value is a range or
 * single measurement with a unit.
 *
 * Values are canonical ids. The `unit` column on the row is a
 * QuantityUnit id and is expected to be one that makes sense for the
 * kind, though the schema does not enforce the correspondence. The
 * mapper cross-checks: for example, `preferred_ph` should be `ph`,
 * `mature_height` should be a length unit, etc. See PlantNumericFactMapper.
 */
enum class PlantNumericFactKind(
    override val id: String,
) : VocabularyValue {
    MATURE_HEIGHT("mature_height"),
    MATURE_WIDTH("mature_width"),
    ROOT_DEPTH("root_depth"),
    MINIMUM_TEMPERATURE("minimum_temperature"),
    MAXIMUM_TEMPERATURE("maximum_temperature"),
    PREFERRED_PH("preferred_ph"),
    SPACING("spacing"),
    ROW_SPACING("row_spacing"),
    SEED_DEPTH("seed_depth"),
    GERMINATION_TEMPERATURE("germination_temperature"),
    GERMINATION_TIME("germination_time"),
    DAYS_TO_MATURITY("days_to_maturity"),
    YIELD("yield"),
    CONTAINER_SIZE("container_size"),
    HARVEST_FREQUENCY("harvest_frequency"),
    ;

    companion object {
        fun fromId(id: String): PlantNumericFactKind? = entries.firstOrNull { it.id == id }
    }
}
