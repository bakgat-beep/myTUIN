package com.mytuin.gardenplanner.domain.vocabulary

/**
 * ActivityQuantityUnit.
 *
 * ACTIVITY_VOCABULARIES.md §20. All 13 values included.
 */
enum class ActivityQuantityUnit(
    override val id: String,
) : VocabularyValue {
    COUNT("count"),
    BUNCH("bunch"),
    BASKET("basket"),
    CONTAINER("container"),
    LITRE("litre"),
    MILLILITRE("millilitre"),
    KILOGRAM("kilogram"),
    GRAM("gram"),
    METRE("metre"),
    SQUARE_METRE("square_metre"),
    HOUR("hour"),
    MINUTE("minute"),
    OTHER("other"),
    ;

    companion object {
        fun fromId(id: String): ActivityQuantityUnit? = entries.firstOrNull { it.id == id }
    }
}
