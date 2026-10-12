package com.mytuin.gardenplanner.domain.vocabulary

/**
 * SoilDrainage.
 *
 * SOIL_VOCABULARIES.md §5. All 6 values included.
 *
 * How readily excess water leaves the soil. A plant's
 * `soil_drainage` multi-value references these ids.
 *
 * A qualitative classification. Actual observations such as "water
 * remained for approximately 6 hours" should be stored as structured
 * observations or measurements, not as a drainage value.
 */
enum class SoilDrainage(
    override val id: String,
) : VocabularyValue {
    VERY_FAST("very_fast"),
    FAST("fast"),
    MODERATE("moderate"),
    SLOW("slow"),
    VERY_SLOW("very_slow"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): SoilDrainage? = entries.firstOrNull { it.id == id }
    }
}
