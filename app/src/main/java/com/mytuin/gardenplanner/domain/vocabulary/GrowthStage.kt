package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GrowthStage.
 *
 * PLANT_VOCABULARIES §6. All 14 values included.
 *
 * Developmental stage of a plant. Used on plant-problem
 * relationships to say "this susceptibility applies at stage X"
 * (V1_DATABASE_SCHEMA §23).
 *
 * Not every plant uses every stage. Stage-specific timing and
 * requirements remain separate structured information where
 * appropriate.
 */
enum class GrowthStage(
    override val id: String,
) : VocabularyValue {
    SEED("seed"),
    GERMINATION("germination"),
    SEEDLING("seedling"),
    ESTABLISHMENT("establishment"),
    VEGETATIVE("vegetative"),
    BUD_FORMATION("bud_formation"),
    FLOWERING("flowering"),
    FRUITING("fruiting"),
    SEED_DEVELOPMENT("seed_development"),
    MATURITY("maturity"),
    DORMANCY("dormancy"),
    SENESCENCE("senescence"),
    HARVESTABLE("harvestable"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): GrowthStage? = entries.firstOrNull { it.id == id }
    }
}
