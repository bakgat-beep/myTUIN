package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SoilTexture.
 *
 * SOIL_VOCABULARIES.md §3. All 10 values included.
 *
 * Relative proportions of mineral particle-size classes. A plant's
 * `preferred_soil` multi-value references these ids.
 *
 * Where a formal soil classification system is used, the underlying
 * system and method should be recorded separately.
 */
enum class SoilTexture(
    override val id: String,
) : VocabularyValue {
    SAND("sand"),
    SANDY_LOAM("sandy_loam"),
    LOAM("loam"),
    SILT_LOAM("silt_loam"),
    SILT("silt"),
    CLAY_LOAM("clay_loam"),
    SANDY_CLAY("sandy_clay"),
    SILTY_CLAY("silty_clay"),
    CLAY("clay"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SoilTexture? = entries.firstOrNull { it.id == id }
    }
}
