package com.mytuin.gardenplanner.domain.vocabulary

/**
 * MaturityClassification.
 *
 * PLANT_VOCABULARIES.md §11. Four values.
 *
 * A broad early/mid/late classification. Does not replace actual
 * days-to-maturity, dates or ranges. Where maturity differs
 * substantially by cultivar, store it at cultivar level (§28).
 */
enum class MaturityClassification(
    override val id: String,
) : VocabularyValue {
    EARLY("early"),
    MID_SEASON("mid_season"),
    LATE("late"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): MaturityClassification? = entries.firstOrNull { it.id == id }
    }
}
