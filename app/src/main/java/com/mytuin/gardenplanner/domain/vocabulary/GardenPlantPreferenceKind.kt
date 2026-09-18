package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Kind of plant-list preference.
 *
 * DEC-042 names "favourite plants" and "plants to avoid". Both are
 * the same shape — a Plant the gardener has flagged — so they share
 * one table with a kind discriminator (A176=a).
 *
 * Canonical ids only (DEC-040).
 */
enum class GardenPlantPreferenceKind(override val id: String) : VocabularyValue {
    FAVOURITE("favourite"),
    AVOID("avoid"),
    ;

    companion object {
        fun fromId(id: String): GardenPlantPreferenceKind? =
            entries.firstOrNull { it.id == id }
    }
}