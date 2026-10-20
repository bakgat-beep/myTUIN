package com.mytuin.gardenplanner.domain.vocabulary

/**
 * RecommendationSubjectType.
 *
 * R2. The kind of entity a Recommendation is about. Polymorphic
 * `subject_id` has no FK; the pair (subject_type, subject_id) is
 * not enforced by SQLite.
 *
 * Distinct from PlanTargetType despite identical value sets, so the
 * two can evolve independently. See Step 6 commit message.
 */
enum class RecommendationSubjectType(
    override val id: String,
) : VocabularyValue {
    PLANT("plant"),
    CULTIVAR("cultivar"),
    PLANT_INSTANCE("plant_instance"),
    GROWING_SPACE("growing_space"),
    AREA("area"),
    SPATIAL_OBJECT("spatial_object"),
    ;

    companion object {
        fun fromId(id: String): RecommendationSubjectType? = entries.firstOrNull { it.id == id }
    }
}
