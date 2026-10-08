package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlanTargetType.
 *
 * PL2 (a). Classifies what kind of entity a plan_target row points
 * to. V1_DATABASE_SCHEMA §41 lists Plant, PlantInstance, GrowingSpace,
 * Area, and "another relevant garden object". This enum covers those
 * plus Cultivar and SpatialObject.
 *
 * The target_id column is untyped (String), so no foreign key can be
 * declared on it. Existence is pre-checked by
 * PlanRepositoryImpl.addTarget based on the target_type value. The
 * tradeoff is recorded in the DEC for Step 3f.
 */
enum class PlanTargetType(
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
        fun fromId(id: String): PlanTargetType? = entries.firstOrNull { it.id == id }
    }
}
