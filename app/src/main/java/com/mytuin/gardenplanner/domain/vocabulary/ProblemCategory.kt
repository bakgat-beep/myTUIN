package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ProblemCategory.
 *
 * PROBLEM_VOCABULARIES.md §3. All 13 values included.
 *
 * `unknown` is a value: it distinguishes "recorded as an unknown
 * category" from "no category recorded" (CORE_VOCABULARIES §4).
 * However, §3 lists category as required on the entity, so in
 * practice a caller supplies a value or `unknown`.
 *
 * This is a coarse classification. Specific pests, diseases, and
 * other problem entities are knowledge records, not vocabulary
 * values (§4, §5, §52).
 */
enum class ProblemCategory(
    override val id: String,
) : VocabularyValue {
    PEST("pest"),
    DISEASE("disease"),
    NUTRIENT_ISSUE("nutrient_issue"),
    WATER_STRESS("water_stress"),
    ENVIRONMENTAL_STRESS("environmental_stress"),
    PHYSICAL_DAMAGE("physical_damage"),
    WEED("weed"),
    GROWTH_PROBLEM("growth_problem"),
    SOIL_PROBLEM("soil_problem"),
    WEATHER_DAMAGE("weather_damage"),
    ANIMAL_DAMAGE("animal_damage"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): ProblemCategory? = entries.firstOrNull { it.id == id }
    }
}
