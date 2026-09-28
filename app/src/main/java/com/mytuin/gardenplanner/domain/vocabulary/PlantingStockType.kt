package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PlantingStockType.
 *
 * PLANT_VOCABULARIES.md §26. Twelve values. Includes `unknown`
 * because §26 provides it: "recorded as unknown" is a distinct state
 * from "not recorded" (CORE_VOCABULARIES §4).
 *
 * Distinct from propagation method (§10): stock describes what is
 * planted; propagation method describes how the plant was propagated.
 */
enum class PlantingStockType(
    override val id: String,
) : VocabularyValue {
    SEED("seed"),
    SEEDLING("seedling"),
    PLUG("plug"),
    BARE_ROOT("bare_root"),
    BULB("bulb"),
    TUBER("tuber"),
    RHIZOME("rhizome"),
    CUTTING("cutting"),
    DIVISION("division"),
    GRAFTED_PLANT("grafted_plant"),
    ESTABLISHED_PLANT("established_plant"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PlantingStockType? = entries.firstOrNull { it.id == id }
    }
}
