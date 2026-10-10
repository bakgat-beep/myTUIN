package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GrowthRate.
 *
 * PLANT_VOCABULARIES.md §8. Five values.
 *
 * A planning classification only. Does not replace measured growth
 * rates, mature dimensions or cultivar-specific information.
 */
enum class GrowthRate(
    override val id: String,
) : VocabularyValue {
    SLOW("slow"),
    MODERATE("moderate"),
    FAST("fast"),
    VARIABLE("variable"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): GrowthRate? = entries.firstOrNull { it.id == id }
    }
}
