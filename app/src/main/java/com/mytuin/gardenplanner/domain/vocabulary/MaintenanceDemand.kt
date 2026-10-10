package com.mytuin.gardenplanner.domain.vocabulary

/**
 * MaintenanceDemand.
 *
 * PLANT_VOCABULARIES.md §21. Four values.
 *
 * A summary classification. Specific maintenance requirements remain
 * plant knowledge and activity data.
 */
enum class MaintenanceDemand(
    override val id: String,
) : VocabularyValue {
    LOW("low"),
    MODERATE("moderate"),
    HIGH("high"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromId(id: String): MaintenanceDemand? = entries.firstOrNull { it.id == id }
    }
}
