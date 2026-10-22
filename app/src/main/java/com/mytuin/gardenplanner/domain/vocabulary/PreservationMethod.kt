package com.mytuin.gardenplanner.domain.vocabulary

/**
 * PreservationMethod.
 *
 * PLANT_VOCABULARIES §41 `preservation_methods`. Multi-value; a
 * plant may be suitable for several preservation methods.
 */
enum class PreservationMethod(
    override val id: String,
) : VocabularyValue {
    CANNING("canning"),
    FREEZING("freezing"),
    DRYING("drying"),
    FERMENTING("fermenting"),
    PICKLING("pickling"),
    STORING("storing"),
    OTHER("other"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): PreservationMethod? = entries.firstOrNull { it.id == id }
    }
}
