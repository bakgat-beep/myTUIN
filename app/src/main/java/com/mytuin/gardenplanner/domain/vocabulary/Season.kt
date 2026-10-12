package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Season.
 *
 * CORE_VOCABULARIES.md §9. Six values.
 *
 * Conceptual seasons interpreted according to geographic context and
 * hemisphere. The application must not assume that the same months
 * represent the same season everywhere.
 */
enum class Season(
    override val id: String,
) : VocabularyValue {
    SPRING("spring"),
    SUMMER("summer"),
    AUTUMN("autumn"),
    WINTER("winter"),
    YEAR_ROUND("year_round"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): Season? = entries.firstOrNull { it.id == id }
    }
}
