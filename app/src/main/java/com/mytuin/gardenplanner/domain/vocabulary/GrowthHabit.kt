package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GrowthHabit.
 *
 * PLANT_VOCABULARIES.md §7. All 17 values included.
 *
 * General physical growth form. Multiple values may apply. Does not
 * replace measured mature dimensions.
 */
enum class GrowthHabit(
    override val id: String,
) : VocabularyValue {
    UPRIGHT("upright"),
    SPREADING("spreading"),
    BUSHY("bushy"),
    CLUMPING("clumping"),
    ROSETTE("rosette"),
    TRAILING("trailing"),
    CLIMBING("climbing"),
    VINING("vining"),
    TWINING("twining"),
    CREEPING("creeping"),
    PROSTRATE("prostrate"),
    MOUNDING("mounding"),
    TUFTED("tufted"),
    TREE("tree"),
    SHRUB("shrub"),
    SUBSHRUB("subshrub"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): GrowthHabit? = entries.firstOrNull { it.id == id }
    }
}
