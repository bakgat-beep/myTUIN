package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RecommendationReasonType.
 *
 * PLANNING_VOCABULARIES.md §8. Twenty-three values.
 *
 * Structured explanations for why a factor received its result. The
 * UI translates these into readable reasons; the stored id remains
 * canonical. R4: stored as `reason_type` on RecommendationFactor,
 * not as a separate reasons entity.
 */
enum class RecommendationReasonType(
    override val id: String,
) : VocabularyValue {
    // Positive / supporting
    LIGHT_MATCH("light_match"),
    WATER_MATCH("water_match"),
    SOIL_MATCH("soil_match"),
    PH_MATCH("ph_match"),
    FERTILITY_MATCH("fertility_match"),
    TEMPERATURE_MATCH("temperature_match"),
    TIMING_MATCH("timing_match"),
    SPACE_AVAILABLE("space_available"),
    ROTATION_COMPATIBLE("rotation_compatible"),
    INFRASTRUCTURE_AVAILABLE("infrastructure_available"),
    POSITIVE_HISTORY("positive_history"),
    RELATIONSHIP_SUPPORT("relationship_support"),
    USER_PREFERENCE("user_preference"),

    // Negative / limiting
    ROTATION_RISK("rotation_risk"),
    SPACE_CONSTRAINT("space_constraint"),
    SOIL_CONSTRAINT("soil_constraint"),
    TIMING_CONSTRAINT("timing_constraint"),
    TEMPERATURE_CONSTRAINT("temperature_constraint"),
    WATER_CONSTRAINT("water_constraint"),
    PERENNIAL_MATURITY_CONSTRAINT("perennial_maturity_constraint"),
    ENVIRONMENTAL_CONSTRAINT("environmental_constraint"),
    NEGATIVE_HISTORY("negative_history"),

    // Information-related
    MISSING_INFORMATION("missing_information"),
    ;

    companion object {
        fun fromId(id: String): RecommendationReasonType? = entries.firstOrNull { it.id == id }
    }
}
