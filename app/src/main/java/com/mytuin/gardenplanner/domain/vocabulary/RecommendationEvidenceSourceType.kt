package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RecommendationEvidenceSourceType.
 *
 * R3. The kind of entity a piece of RecommendationEvidence refers
 * to. Polymorphic `source_id` has no FK.
 */
enum class RecommendationEvidenceSourceType(
    override val id: String,
) : VocabularyValue {
    PLANT("plant"),
    CULTIVAR("cultivar"),
    GROWING_SPACE("growing_space"),
    OBSERVATION("observation"),
    MEASUREMENT("measurement"),
    PLANT_INSTANCE("plant_instance"),
    ACTIVITY("activity"),
    PROBLEM("problem"),
    HARVEST("harvest"),
    GARDEN_PREFERENCE("garden_preference"),
    SOURCE("source"),
    ;

    companion object {
        fun fromId(id: String): RecommendationEvidenceSourceType? = entries.firstOrNull { it.id == id }
    }
}
